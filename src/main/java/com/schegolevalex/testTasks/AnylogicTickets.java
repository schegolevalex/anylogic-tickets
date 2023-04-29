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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

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

        System.out.println(getAverageFlyTime(tickets));
        System.out.println(get90Percentile(tickets));
    }

    private static LocalTime getAverageFlyTime(Ticket[] tickets) {

        Arrays.stream(tickets);
        return null;
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
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm")));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("H:mm")));

        mapper.registerModule(javaTimeModule);
        return mapper;
    }
}
