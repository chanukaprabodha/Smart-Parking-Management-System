package com.example.parkingspaceservice.service;

import com.example.parkingspaceservice.dto.ParkingDTO;
import com.example.parkingspaceservice.dto.UserDTO;
import com.example.parkingspaceservice.dto.UserRole;
import com.example.parkingspaceservice.entity.Parking;
import com.example.parkingspaceservice.repo.ParkingRepository;
import com.example.parkingspaceservice.util.IdGenerator;
import com.example.parkingspaceservice.util.VarList;
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
public class ParkingServiceImpl implements ParkingService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ParkingRepository parkingRepo;

    @Override
    public int saveParkingPlace(ParkingDTO parkingDTO) {
        try {
            if (isUserExists(parkingDTO.getEmail().toLowerCase()) && isUserAdmin(parkingDTO.getEmail().toLowerCase())) {
                if (parkingRepo.existsParkingByLocation(parkingDTO.getLocation())) {
                    return VarList.All_Ready_Added;
                }
                parkingDTO.setId(IdGenerator.generateId("PARK_"));
                parkingRepo.save(modelMapper.map(parkingDTO, Parking.class));
                return VarList.Created;

            } else {
                return VarList.Not_Found;
            }

        } catch (Exception e) {
            throw new RuntimeException("Error saving parking place: " + e.getMessage(), e);
        }
    }

    @Override
    public int updateParkingPlace(ParkingDTO parking) {
        try {
            if (isUserExists(parking.getEmail().toLowerCase()) &&
                    isUserAdmin(parking.getEmail().toLowerCase()) &&
                    parkingRepo.existsById(parking.getId())) {

                parkingRepo.save(modelMapper.map(parking, Parking.class));
                return VarList.Created;

            } else {
                return VarList.Not_Found;
            }

        } catch (Exception e) {
            throw new RuntimeException("Error updating parking place: " + e.getMessage(), e);
        }
    }

    @Override
    public int deleteParkingPlace(String id, String email, String location) {
        try {
            if (isUserExists(email.toLowerCase()) && isUserAdmin(email.toLowerCase())) {
                if (parkingRepo.existsById(id)) {
                    parkingRepo.deleteById(id);
                    return VarList.OK;
                }
            }
            return VarList.Not_Found;

        } catch (Exception e) {
            throw new RuntimeException("Error deleting parking place: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ParkingDTO> getAllParkingPlaces() {
        try {
            List<Parking> parkingList = parkingRepo.findAll();
            return parkingList.stream()
                    .map(parking -> modelMapper.map(parking, ParkingDTO.class))
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching all parking places: " + e.getMessage(), e);
        }
    }

    public boolean isUserExists(String email) {
        try {
            ResponseEntity<UserDTO> response = restTemplate.getForEntity(
                    "http://user-service/api/v1/users/" + email,
                    UserDTO.class
            );
            if (response.getStatusCode().is2xxSuccessful()) {
                UserDTO user = response.getBody();
                return user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.CUSTOMER;
            }
            return false;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        }
    }

    public boolean isUserAdmin(String email) {
        try {
            ResponseEntity<UserDTO> response = restTemplate.getForEntity(
                    "http://user-service/api/v1/users/" + email,
                    UserDTO.class
            );
            if (response.getStatusCode().is2xxSuccessful()) {
                UserDTO user = response.getBody();
                return user.getRole() == UserRole.ADMIN;
            }
            return false;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        }
    }
}
