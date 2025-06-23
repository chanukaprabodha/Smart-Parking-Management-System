package com.example.vehicleservice.repo;

import com.example.vehicleservice.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {

    boolean existsByVehicleNumber(String vehicleNumber);

    Vehicle findByVehicleNumber(String vehicleNumber);

    void deleteByVehicleNumber(String vehicleNumber);
}
