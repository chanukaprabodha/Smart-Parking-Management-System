package com.example.paymentservice.service;

import com.example.paymentservice.dto.PaymentDTO;
import com.example.paymentservice.dto.ResponseDTO;
import com.example.paymentservice.dto.UserDTO;
import com.example.paymentservice.entity.Parking;
import com.example.paymentservice.entity.Payment;
import com.example.paymentservice.repo.ParkingRepository;
import com.example.paymentservice.repo.PaymentRepository;
import com.example.paymentservice.util.IdGenerator;
import com.example.paymentservice.util.VarList;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepo;

    @Autowired
    private ParkingRepository parkingRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    @Transactional
    public ResponseDTO savePayment(PaymentDTO paymentDTO) {
        paymentDTO.setPaymentDate(Date.valueOf(LocalDate.now()));
        paymentDTO.setPaymentTime(Time.valueOf(LocalTime.now()));
        paymentDTO.setBookingDate(Date.valueOf(LocalDate.now()));

        Parking location = parkingRepo.findByLocation(paymentDTO.getParkingLocation().toLowerCase());

        if (!isUserExists(paymentDTO.getUserEmail())) {
            return new ResponseDTO(VarList.Not_Found,
                    "User not found",
                    null);
        }
        if (location == null) {
            return new ResponseDTO(VarList.Not_Found,
                    "Parking location not found",
                    null);
        }
        if (location.isAvailable()) {
            paymentDTO.setAmount(location.getPayAmount());
            paymentDTO.setParkingLocation(location.getLocation().toLowerCase());
            paymentDTO.setPaymentId(IdGenerator.generateId("PAY_"));

            try {
                Payment payment = modelMapper.map(paymentDTO, Payment.class);
                paymentRepo.save(payment);
                location.setAvailable(false);
                parkingRepo.save(location);

                return new ResponseDTO(VarList.Created,
                        "Payment and transaction successfully created",
                        null);

            } catch (Exception e) {
                throw new RuntimeException("Failed to save payment: " + e.getMessage(), e);
            }

        } else {
            return new ResponseDTO(VarList.Conflict,
                    "Parking space is not available",
                    null);
        }
    }

    @Override
    public List<PaymentDTO> getAllPayments() {
        try {
            List<Payment> payments = paymentRepo.findAll();
            return payments.stream()
                    .map(payment -> modelMapper.map(payment, PaymentDTO.class))
                    .toList();
        } catch (Exception e) {
            System.err.println("Error retrieving payments: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public List<PaymentDTO> getAllPaymentsByEmail(String email) {

        try {
            List<Payment> payments = paymentRepo.findByUserEmail(email);
            return payments.stream()
                    .map(payment -> modelMapper.map(payment, PaymentDTO.class))
                    .toList();
        } catch (Exception e) {
            System.err.println("Error retrieving payments by email: " + e.getMessage());
            return List.of();
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
            return false;
        }
    }
}
