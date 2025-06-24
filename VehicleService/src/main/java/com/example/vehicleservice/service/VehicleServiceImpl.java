package com.example.vehicleservice.service;

import com.example.vehicleservice.dto.UserDTO;
import com.example.vehicleservice.dto.VehicleDTO;
import com.example.vehicleservice.entity.Vehicle;
import com.example.vehicleservice.repo.VehicleRepository;
import com.example.vehicleservice.util.IdGenerator;
import com.example.vehicleservice.util.VarList;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Transactional
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Override
    public int addVehicle(VehicleDTO vehicleDTO) {
        if (isUserExists(vehicleDTO.getEmail())) {
            return VarList.Not_Found;

        } else if (vehicleRepository.existsByVehicleNumber(vehicleDTO.getVehicleNumber())) {
            return VarList.All_Ready_Added;

        } else {
            try {
                vehicleDTO.setId(IdGenerator.generateId("VEH_"));
                Vehicle vehicleEntity = modelMapper.map(vehicleDTO, Vehicle.class);
                vehicleEntity.setEmail(vehicleDTO.getEmail().toLowerCase());
                vehicleRepository.save(vehicleEntity);
                return VarList.Created;

            } catch (Exception e) {
                throw new RuntimeException("Error saving vehicle: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public int updateVehicle(VehicleDTO vehicleDTO) {
        if (!vehicleRepository.existsById(vehicleDTO.getId()) || ! isUserExists(vehicleDTO.getEmail().toLowerCase())) {
            return VarList.Not_Found;
        }
        try {
            Vehicle vehicleEntity = modelMapper.map(vehicleDTO, Vehicle.class);
            vehicleEntity.setEmail(vehicleDTO.getEmail().toLowerCase());
            vehicleRepository.save(vehicleEntity);
            return VarList.OK;

        } catch (Exception e) {
            throw new RuntimeException("Error updating vehicle: " + e.getMessage(), e);
        }
    }

    @Override
    public int deleteVehicle(String vehicleNumber) {
        if (!vehicleRepository.existsByVehicleNumber(vehicleNumber)) {
            return VarList.Not_Found;
        }
        try {
            vehicleRepository.deleteByVehicleNumber(vehicleNumber);
            return VarList.OK;

        } catch (Exception e) {
            throw new RuntimeException("Error deleting vehicle: " + e.getMessage(), e);
        }
    }

    @Override
    public List<VehicleDTO> getAllVehicles() {
        try {
            List<Vehicle> vehicles = vehicleRepository.findAll();
            return vehicles.stream()
                    .map(vehicle -> modelMapper.map(vehicle, VehicleDTO.class))
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching vehicles: " + e.getMessage(), e);
        }
    }

    public boolean isUserExists(String email) {
        try {
            ResponseEntity<UserDTO> response = restTemplate.getForEntity(
                    "http://user-service/api/v1/users/" + email,
                    UserDTO.class
            );
            return response.getStatusCode().is2xxSuccessful();
        } catch (HttpClientErrorException.NotFound e) {
            return true;
        }
    }
}
