package com.example.parkingspaceservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Parking {
    @Id
    private String id;
    @Column(nullable = false, unique = true)
    private String location;
    @Column(nullable = false)
    private String city;
    private boolean available;
    @Column(nullable = false)
    private String email;
    private int payAmount;
}
