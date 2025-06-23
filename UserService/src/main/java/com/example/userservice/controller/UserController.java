package com.example.userservice.controller;

import com.example.userservice.dto.ResponseDTO;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.entity.User;
import com.example.userservice.service.UserService;
import com.example.userservice.util.IdGenerator;
import com.example.userservice.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(value = "/addUser")
    //http://localhost:8081/api/v1/users/addUser
    public ResponseEntity<ResponseDTO> addUser(@RequestBody UserDTO user) {
        try {
            user.setId(IdGenerator.generateId("USR_"));
            user.setCreatedAt(Date.valueOf(LocalDate.now()));

            int res = userService.addUser(user);
            switch (res) {
                case VarList.Created -> {
                    return ResponseEntity.ok(new ResponseDTO(
                            VarList.Created,
                            "User added successfully",
                            user));
                }
                case VarList.All_Ready_Added -> {
                    return ResponseEntity.ok(new ResponseDTO(
                            VarList.All_Ready_Added,
                            "User already exists",
                            null));
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(
                            VarList.Internal_Server_Error,
                            "Error adding user",
                            null));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error while adding user: " + e.getMessage());
        }
    }

    @PutMapping(value = "/updateUser")
    //http://localhost:8081/api/v1/users/updateUser
    public ResponseEntity<ResponseDTO> updateUser(@RequestBody UserDTO user) {
        user.setCreatedAt(Date.valueOf(LocalDate.now()));
        try {
            int res = userService.updateUser(user);
            switch (res) {
                case VarList.OK -> {
                    return ResponseEntity.ok(new ResponseDTO(
                            VarList.OK,
                            "User updated successfully",
                            user));
                }
                case VarList.Not_Found -> {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(
                            VarList.Not_Found,
                            "User not found",
                            null));
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(
                            VarList.Internal_Server_Error,
                            "Error updating user",
                            null));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while updating user: " + e.getMessage());
        }
    }

    @DeleteMapping(value = "/deleteUser")
    //http://localhost:8081/api/v1/users/deleteUser
    public ResponseEntity<ResponseDTO> deleteUser(@RequestParam String email, @RequestParam String password) {
        try {
            int res = userService.deleteUser(email, password);
            switch (res) {
                case VarList.OK -> {
                    return ResponseEntity.ok(new ResponseDTO(
                            VarList.OK,
                            "User deleted successfully",
                            null));
                }
                case VarList.Not_Found -> {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(
                            VarList.Not_Found,
                            "User not found",
                            null));
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(
                            VarList.Internal_Server_Error,
                            "Error deleting user",
                            null));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting user: " + e.getMessage());
        }
    }

    @GetMapping(value = "/getAllUser")
    //http://localhost:8081/api/v1/users/getAllUser
    public ResponseEntity<ResponseDTO> getAllUser() {
        try {
            return ResponseEntity.ok(new ResponseDTO(
                    VarList.OK,
                    "All users retrieved successfully",
                    userService.getAllUser()));
        } catch (Exception e) {
            throw new RuntimeException("Error while retrieving all users: " + e.getMessage());
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        Optional<UserDTO> user = userService.getUserByEmail(email);
        if (user != null) {
            return ResponseEntity.ok(modelMapper.map(user.get(), UserDTO.class));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
