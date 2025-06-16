package com.air.air.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data // getter/setter, toString, equals, hashCode 자동 생성
@NoArgsConstructor // 기본 생성자 자동 생성
@Entity
@Table(name = "booking")
public class BookingInfo {
    @Id
    @Column(name = "booking_id")
    private String bookingId;

    @Column(name = "flight_number")
    private String flightNumber;

    private String airline;
    private String departure;
    private String arrival;

    @Column(name = "departure_time")
    private LocalDateTime departureTime;

    @Column(name = "arrival_time")
    private LocalDateTime arrivalTime;

    private int passengers;
    private BigDecimal price;

    @Column(name = "booking_date")
    private LocalDate bookingDate;

    private String username;
}
