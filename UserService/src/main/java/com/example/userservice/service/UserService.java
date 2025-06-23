package com.example.userservice.service;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    int addUser(UserDTO user);

    int updateUser(UserDTO user);

    int deleteUser(String email, String password);

    List<UserDTO> getAllUser();

    Optional<User> findByEmail(String email);

    Optional<UserDTO> getUserByEmail(String email);
}

