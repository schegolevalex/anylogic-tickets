package com.schegolevalex.testTasks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;

import java.io.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AnylogicTickets {

    public static void main(String[] args) {
        String resourceName = "tickets.json";
        String cityFrom = "Владивосток";
        String cityTo = "Тель-Авив";

        AnylogicTickets app = new AnylogicTickets();

        InputStream fileFromResourceAsStream = app.getFileFromResourceAsStream(resourceName);
        StringBuilder json = getJsonStringFromInputStream(fileFromResourceAsStream);
        ObjectMapper mapper = getObjectMapper();
        JsonNode jsonNode;
        List<Ticket> tickets;
        try {
            jsonNode = mapper.readTree(json.toString());
            JsonNode ticketsNode = jsonNode.get("tickets");
            tickets = mapper.readValue(ticketsNode.toString(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        long seconds = getAverageFlyTime(tickets, cityFrom, cityTo).toSeconds();
        System.out.printf("Среднее время полета между городом %s и городом %s составляет %d:%02d:%02d%n"
                , cityFrom, cityTo, seconds / 3600, (seconds % 3600) / 60, (seconds % 60));

        long percentile90 = get90Percentile(tickets, cityFrom, cityTo).toSeconds();
        System.out.printf("90-й процентиль времени полета между городом %s и городом %s составляет %d:%02d:%02d%n"
                , cityFrom, cityTo, percentile90 / 3600, (percentile90 % 3600) / 60, (percentile90 % 60));
    }

    private static Duration getAverageFlyTime(List<Ticket> tickets, String originName, String destinationName) {
        List<Ticket> filteredTicketList = getFilteredTicketList(tickets, originName, destinationName);
        List<Duration> durationsList = getDurationsList(filteredTicketList);
        return durationsList.stream().reduce(Duration.ZERO, Duration::plus).dividedBy(durationsList.size());
    }

    private InputStream getFileFromResourceAsStream(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }

    private static Duration get90Percentile(List<Ticket> tickets, String originName, String destinationName) {
        List<Ticket> filteredTicketList = getFilteredTicketList(tickets, originName, destinationName);
        List<Duration> durationsList = getDurationsList(filteredTicketList);
        List<Duration> sortedDurationsList = durationsList.stream().sorted().toList();
        double i = 0.9 * sortedDurationsList.size();
        return sortedDurationsList.get((int) Math.ceil(i)-1);
    }

    private static List<Duration> getDurationsList(List<Ticket> filteredTicketList) {
        return filteredTicketList.stream()
                .map(ticket -> {
                    LocalDateTime localDateTimeDeparture = LocalDateTime.of(ticket.getDepartureDate(), ticket.getDepartureTime());
                    LocalDateTime localDateTimeArrival = LocalDateTime.of(ticket.getArrivalDate(), ticket.getArrivalTime());
                    return Duration.between(localDateTimeDeparture, localDateTimeArrival);
                })
                .toList();
    }

    private static List<Ticket> getFilteredTicketList(List<Ticket> tickets, String originName, String destinationName) {
        return tickets.stream()
                .filter(ticket -> ticket.getOriginName().equals(originName) && ticket.getDestinationName().equals(destinationName))
                .toList();
    }

    private static StringBuilder getJsonStringFromInputStream(InputStream inputStream) {
        StringBuilder json = new StringBuilder();
        String line;

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim().replaceAll("\ufeff", "");
                json.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return json;
    }

    private static ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("dd.MM.yy")));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("H:mm")));

        mapper.registerModule(javaTimeModule);
        return mapper;
    }
}
