package com.example.parkingspaceservice.service;

import com.example.parkingspaceservice.dto.ParkingDTO;

import java.util.List;

public interface ParkingService {
    int saveParkingPlace(ParkingDTO parkingDTO);

    int updateParkingPlace(ParkingDTO parking);

    int deleteParkingPlace(String id, String email, String location);

    List<ParkingDTO> getAllParkingPlaces();
}
