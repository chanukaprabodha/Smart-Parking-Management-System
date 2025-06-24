package com.example.paymentservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Payment {
    @Id
    private String paymentId;
    @Column(nullable = false)
    private String vehicleNumber;
    @Column(nullable = false)
    private String userEmail;
    @Column(nullable = false)
    private String parkingLocation;
    @Column(nullable = false)
    private int amount;
    @Column(nullable = false)
    private Date paymentDate;
    @Column(nullable = false)
    private Date bookingDate;
    @Column(nullable = false)
    private Time paymentTime;
}
