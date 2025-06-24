package com.example.parkingspaceservice.controller;

import com.example.parkingspaceservice.dto.ParkingDTO;
import com.example.parkingspaceservice.dto.ResponseDTO;
import com.example.parkingspaceservice.service.ParkingService;
import com.example.parkingspaceservice.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/parking")
public class ParkingController {

    @Autowired
    private ParkingService parkingService;

    @PostMapping(value = "/saveParking")
    //http://localhost:8083/api/v1/parking/saveParking

    public ResponseEntity<ResponseDTO> saveParkingPlace(@RequestBody ParkingDTO parking) {
        try {
            int res = parkingService.saveParkingPlace(parking);
            switch (res) {
                case VarList.Created -> {
                    return ResponseEntity.ok(new ResponseDTO(
                            VarList.Created,
                            "Parking place added successfully",
                            parking));
                }
                case VarList.All_Ready_Added -> {
                    return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(new ResponseDTO(
                            VarList.All_Ready_Added,
                            "Parking already exists",
                            null));
                }
                case VarList.Not_Found -> {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(
                            VarList.Not_Found,
                            "User not found or user is not an admin",
                            null));
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ResponseDTO(
                                    VarList.Internal_Server_Error,
                                    "Error saving Parking",
                                    null));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping(value = "/updateParking")
    //http://localhost:8083/api/v1/parking/updateParking

    public ResponseEntity<ResponseDTO> updateParkingPlace(@RequestBody ParkingDTO parking) {
        try {
            int res = parkingService.updateParkingPlace(parking);
            switch (res) {
                case VarList.Created -> {
                    return ResponseEntity.ok(new ResponseDTO(
                            VarList.Created,
                            "Parking updated successfully",
                            parking));
                }
                case VarList.Not_Found -> {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(
                            VarList.Not_Found,
                            "Parking not found or user is not an admin",
                            null));
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ResponseDTO(
                                    VarList.Internal_Server_Error,
                                    "Error updating Parking",
                                    null));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error updating parking: " + e.getMessage(), e);
        }
    }

    @DeleteMapping(value = "/deleteParking")
    //http://localhost:8083/api/v1/parking/deleteParking

    public ResponseEntity<ResponseDTO> deleteParkingPlace(@RequestBody ParkingDTO parking) {
        try {
            int res = parkingService.deleteParkingPlace(parking.getId(), parking.getEmail(), parking.getLocation());
            switch (res) {
                case VarList.OK -> {
                    return ResponseEntity.ok(new ResponseDTO(
                            VarList.Created,
                            "Parking deleted successfully",
                            parking));
                }
                case VarList.Not_Found -> {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(
                            VarList.Not_Found,
                            "Parking not found or user is not an admin",
                            null));
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ResponseDTO(
                                    VarList.Internal_Server_Error,
                                    "Error deleting Parking",
                                    null));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deleting parking: " + e.getMessage(), e);
        }
    }

    @GetMapping(value = "/getAllParking")
    //http://localhost:8083/api/v1/parking/getAllParking

    public ResponseEntity<ResponseDTO> getAllParking() {
        try {
            return ResponseEntity.ok(new ResponseDTO(
                    VarList.OK,
                    "All parking places fetched successfully",
                    parkingService.getAllParkingPlaces()));
        } catch (Exception e) {
            throw new RuntimeException("Error fetching all parking places: " + e.getMessage(), e);
        }
    }


}
