package com.example.userservice.service;

import com.example.userservice.dto.UserDTO;

import java.util.List;

public interface UserService {
    int addUser(UserDTO user);

    int updateUser(UserDTO user);

    int deleteUser(String email, String password);

    List<UserDTO> getAllUser();
}

