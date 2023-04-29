package com.schegolevalex.testTasks;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * Hello world!
 */
public class AnylogicTickets {

    @SneakyThrows
    public static void main(String[] args) {
        String filePath = "src/main/resources/tickets.json";
        StringBuilder json = getJsonStringFromFilePath(filePath);

        ObjectMapper mapper = getObjectMapper();
        JsonNode jsonNode = mapper.readTree(json.toString());
        JsonNode ticketsNode = jsonNode.get("tickets");
        Ticket[] tickets = mapper.readValue(ticketsNode.toString(), new TypeReference<>() {
        });

        long seconds = getAverageFlyTime(tickets, "Владивосток", "Тель-Авив").toSeconds();

        System.out.printf("%d:%02d:%02d%n", seconds / 3600, (seconds % 3600) / 60, (seconds % 60));

        System.out.println(get90Percentile(tickets));
    }

    private static Duration getAverageFlyTime(Ticket[] tickets, String originName, String destinationName) {
        List<Ticket> filteredTicketList = Arrays.stream(tickets)
                .filter(ticket -> ticket.getOriginName().equals(originName) && ticket.getDestinationName().equals(destinationName))
                .toList();

        List<Duration> durationsList = filteredTicketList.stream()
                .map(ticket -> {
                    LocalDateTime localDateTimeDeparture = LocalDateTime.of(ticket.getDepartureDate(), ticket.getDepartureTime());
                    LocalDateTime localDateTimeArrival = LocalDateTime.of(ticket.getArrivalDate(), ticket.getArrivalTime());
                    return Duration.between(localDateTimeDeparture, localDateTimeArrival);
                })
                .toList();

        return durationsList.stream().reduce(Duration.ZERO, Duration::plus).dividedBy(durationsList.size());
    }

    private static LocalTime get90Percentile(Ticket[] tickets) {
        return null;
    }

    private static StringBuilder getJsonStringFromFilePath(String filePath) {
        StringBuilder json = new StringBuilder();
        String line;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
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
