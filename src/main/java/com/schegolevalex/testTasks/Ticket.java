package com.schegolevalex.testTasks;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Ticket {
    String origin;
    @JsonProperty(value = "origin_name")
    String originName;
    String destination;
    @JsonProperty(value = "destination_name")
    String destinationName;
    @JsonProperty(value = "departure_date")
    LocalDate departureDate;
    @JsonProperty(value = "departure_time")
    LocalTime departureTime;
    @JsonProperty(value = "arrival_date")
    LocalDate arrivalDate;
    @JsonProperty(value = "arrival_time")
    LocalTime arrivalTime;
    String carrier;
    int stops;
    int price;
}
