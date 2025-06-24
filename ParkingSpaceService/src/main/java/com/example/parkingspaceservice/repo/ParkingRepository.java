package com.example.parkingspaceservice.repo;

import com.example.parkingspaceservice.entity.Parking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingRepository extends JpaRepository<Parking, String> {
    boolean existsParkingByLocation(String location);
}
