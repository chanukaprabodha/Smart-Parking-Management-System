package com.example.vehicleservice.service;

import com.example.vehicleservice.dto.VehicleDTO;
import com.example.vehicleservice.entity.Vehicle;

import java.util.List;

public interface VehicleService {
    int addVehicle(VehicleDTO vehicleDTO);

    int updateVehicle(VehicleDTO vehicleDTO);

    int deleteVehicle(String vehicleNumber);

    List<VehicleDTO> getAllVehicles();
}
