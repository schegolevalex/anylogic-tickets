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
        StringBuilder json = new StringBuilder();
        String line;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/tickets.json"))) {
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim().replaceAll("\ufeff", "");
                json.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("dd.MM.yy")));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm")));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("H:mm")));

        mapper.registerModule(javaTimeModule);

        JsonNode jsonNode = mapper.readTree(json.toString());
        JsonNode ticketsNode = jsonNode.get("tickets");
        System.out.println(ticketsNode);

        Ticket[] tickets = mapper.readValue(ticketsNode.toString(), new TypeReference<>() {
        });

        System.out.println(Arrays.toString(tickets));
    }
}
