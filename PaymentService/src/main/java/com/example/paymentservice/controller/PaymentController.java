package com.example.paymentservice.controller;

import com.example.paymentservice.dto.PaymentDTO;
import com.example.paymentservice.dto.ResponseDTO;
import com.example.paymentservice.service.PaymentService;
import com.example.paymentservice.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/savePayment")
    //http://localhost:8084/api/v1/payments/savePayment

    public ResponseEntity<ResponseDTO> savePayment(@RequestBody PaymentDTO paymentDTO) {

        try {
            ResponseDTO res = paymentService.savePayment(paymentDTO);
            return switch (res.getCode()) {
                case VarList.Created -> ResponseEntity.ok(new ResponseDTO(
                        VarList.Created,
                        res.getMessage(),
                        res.getData()));

                case VarList.Not_Found -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(
                        VarList.Not_Found,
                        res.getMessage(),
                        null));

                case VarList.Conflict -> ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseDTO(
                        VarList.Conflict,
                        res.getMessage(),
                        null));

                default -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(
                        VarList.Bad_Request,
                        "Unexpected error occurred",
                        null));
            };

        } catch (Exception e) {
            System.err.println("Error occurred while saving payment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(
                    VarList.Internal_Server_Error,
                    "Internal server error occurred",
                    null));
        }
    }

    @GetMapping(value = "/getAllPayments")
    //http://localhost:8084/api/v1/payments/getAllPayments

    public ResponseEntity<ResponseDTO> getAllPayments() {
        try {
            List<PaymentDTO> payments = paymentService.getAllPayments();
            return ResponseEntity.ok(new ResponseDTO(
                    VarList.OK,
                    "Payments retrieved successfully",
                    payments));

        } catch (Exception e) {
            System.err.println("Error occurred while retrieving payments: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(
                    VarList.Internal_Server_Error,
                    "Internal server error occurred",
                    null));
        }
    }

    @GetMapping(value = "/getPaymentByEmail")
    //http://localhost:8084/api/v1/payments/getPaymentByEmail

    public ResponseEntity<List<PaymentDTO>> getPaymentByEmail(@RequestParam String email) {
        try {
            List<PaymentDTO> payments = paymentService.getAllPaymentsByEmail(email);
            return ResponseEntity.ok(payments);

        } catch (Exception e) {
            System.err.println("Error occurred while retrieving payments by email: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
