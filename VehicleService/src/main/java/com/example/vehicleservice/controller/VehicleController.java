package com.example.vehicleservice.controller;

import com.example.vehicleservice.dto.ResponseDTO;
import com.example.vehicleservice.dto.VehicleDTO;
import com.example.vehicleservice.service.VehicleService;
import com.example.vehicleservice.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping(value = "/addVehicle")
    // http://localhost:8082/api/v1/vehicle/addVehicle

    public ResponseEntity<ResponseDTO> addVehicle(@RequestBody VehicleDTO vehicle) {
        try {
            int res = vehicleService.addVehicle(vehicle);
            switch (res) {
                case VarList.All_Ready_Added -> {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(new ResponseDTO(
                                    VarList.All_Ready_Added,
                                    "Vehicle Already Exists",
                                    null));
                }
                case VarList.Created -> {
                    return ResponseEntity.ok(new ResponseDTO(
                            VarList.Created,
                            "Vehicle created successfully",
                            vehicle));
                }
                case VarList.Not_Found -> {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseDTO(
                                    VarList.Not_Found,
                                    "Not found User",
                                    null));
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ResponseDTO(
                                    VarList.Internal_Server_Error,
                                    "Error saving Vehicle",
                                    null));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while adding vehicle: " + e.getMessage(), e);
        }
    }

    @PutMapping(value = "/updateVehicle")
    //http://localhost:8082/api/v1/vehicles/updateVehicle

    public ResponseEntity<ResponseDTO> updateVehicle(@RequestBody VehicleDTO vehicleDTO) {
        try {
            int res = vehicleService.updateVehicle(vehicleDTO);
            switch (res) {
                case VarList.All_Ready_Added -> {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(new ResponseDTO(
                                    VarList.All_Ready_Added,
                                    "Vehicle Already Exists",
                                    null));
                }
                case VarList.OK -> {
                    return ResponseEntity.ok(new ResponseDTO(
                            VarList.OK,
                            "Vehicle updated successfully",
                            vehicleDTO));
                }
                case VarList.Not_Found -> {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseDTO(
                                    VarList.Not_Found,
                                    "Not found User",
                                    null));
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ResponseDTO(
                                    VarList.Internal_Server_Error,
                                    "Error updating Vehicle",
                                    null));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while updating vehicle: " + e.getMessage(), e);
        }
    }

    @DeleteMapping(value = "/deleteVehicle")
    //http://localhost:8082/api/v1/vehicles/deleteVehicle

    public ResponseEntity<ResponseDTO> deleteVehicle(@RequestParam String vehicleNumber) {
        try {
            int res = vehicleService.deleteVehicle(vehicleNumber);
            switch (res) {
                case VarList.OK -> {
                    return ResponseEntity.ok(new ResponseDTO(
                            VarList.OK,
                            "Vehicle deleted successfully",
                            null));
                }
                case VarList.Not_Found -> {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseDTO(
                                    VarList.Not_Found,
                                    "Vehicle not found",
                                    null));
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ResponseDTO(
                                    VarList.Internal_Server_Error,
                                    "Error deleting Vehicle",
                                    null));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting vehicle: " + e.getMessage(), e);
        }
    }

    @GetMapping(value = "/getAllVehicles")
    //http://localhost:8082/api/v1/vehicles/getAllVehicles

    public ResponseEntity<ResponseDTO> getAllVehicles() {
        try {
            return ResponseEntity.ok(new ResponseDTO(
                    VarList.OK,
                    "Fetched all vehicles successfully",
                    vehicleService.getAllVehicles()));
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching all vehicles: " + e.getMessage(), e);
        }
    }

}
