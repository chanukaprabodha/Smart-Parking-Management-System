package com.example.paymentservice.repo;

import com.example.paymentservice.entity.Parking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingRepository extends JpaRepository<Parking, String> {

    Parking findByLocation(String lowerCase);
}
