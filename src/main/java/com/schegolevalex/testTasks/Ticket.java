package com.schegolevalex.testTasks;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Ticket {
    String origin;
    String originName;
    String destination;
    String destinationName;
    LocalDate departureDate;
    LocalTime departureTime;
    LocalDate arrivalDate;
    LocalTime arrivalTime;
    String carrier;
    int stops;
    int price;


    @JsonProperty(value = "origin_name")
    public void setOriginName(String originName) {
        this.originName = originName;
    }
    @JsonProperty(value = "destination_name")
    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    @JsonProperty(value = "departure_date")
    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    @JsonProperty(value = "departure_time")
    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    @JsonProperty(value = "arrival_date")
    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    @JsonProperty(value = "arrival_time")
    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @JsonProperty(value = "origin_name")
    public String getOriginName() {
        return originName;
    }

    @JsonProperty(value = "destination_name")
    public String getDestinationName() {
        return destinationName;
    }

    @JsonProperty(value = "departure_date")
    public LocalDate getDepartureDate() {
        return departureDate;
    }

    @JsonProperty(value = "departure_time")
    public LocalTime getDepartureTime() {
        return departureTime;
    }

    @JsonProperty(value = "arrival_date")
    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    @JsonProperty(value = "arrival_time")
    public LocalTime getArrivalTime() {
        return arrivalTime;
    }
}
