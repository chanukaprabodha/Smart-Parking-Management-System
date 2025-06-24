package com.example.parkingspaceservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ParkingDTO {
    private String id;
    private String location;
    private String city;
    private boolean available;
    private String email;
    private int payAmount;
}
