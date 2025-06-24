package com.example.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentDTO {
    private String paymentId;
    private String vehicleNumber;
    private String userEmail;
    private String parkingLocation;
    private int amount;
    private Date paymentDate;
    private Date bookingDate;
    private Time paymentTime;
}
