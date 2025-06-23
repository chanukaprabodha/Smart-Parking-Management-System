package com.example.vehicleservice.dto;

import com.example.vehicleservice.entity.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VehicleDTO {
    private String id;
    private VehicleType type;
    private String vehicleNumber;
    private String email;
}
