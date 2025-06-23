package com.example.vehicleservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Vehicle {
    @Id
    private String id;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private VehicleType type;
    @Column(nullable = false, unique = true)
    private String vehicleNumber;
    @Column(nullable = false)
    private String email;
}
