package com.example.userservice.service;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.entity.User;
import com.example.userservice.repo.UserRepository;
import com.example.userservice.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public int addUser(UserDTO user) {

        try {
            if (userRepository.existsByEmail(user.getEmail())) {
                return VarList.All_Ready_Added;
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setEmail(user.getEmail().toLowerCase());
            userRepository.save(modelMapper.map(user, User.class));
            return VarList.Created;

        } catch (Exception e) {
            System.out.println("Error while adding user: " + e.getMessage());
            return VarList.Internal_Server_Error;
        }
    }

    @Override
    public int updateUser(UserDTO user) {
        try {
            if (!userRepository.existsById(user.getId())) {
                return VarList.Not_Found;
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setEmail(user.getEmail().toLowerCase());
            userRepository.save(modelMapper.map(user, User.class));
            return VarList.OK;

        } catch (Exception e) {
            throw new RuntimeException("Error while updating user: " + e.getMessage());
        }
    }

    @Override
    public int deleteUser(String email, String password) {
        try {
            Optional<User> user = userRepository.findByEmail(email.toLowerCase());
            if (user.isEmpty() || !passwordEncoder.matches(password, user.get().getPassword())) {
                return VarList.Not_Found;
            }
            userRepository.delete(user.get());
            return VarList.OK;

        } catch (Exception e) {
            throw new RuntimeException("Error while deleting user: " + e.getMessage());
        }
    }

    @Override
    public List<UserDTO> getAllUser() {
        try {
            List<User> users = userRepository.findAll();
            return users.stream()
                    .map(user -> modelMapper.map(user, UserDTO.class))
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching all users: " + e.getMessage());
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            return userRepository.findByEmail(email.toLowerCase());
        } catch (Exception e) {
            throw new RuntimeException("Error while finding user by email: " + e.getMessage());
        }
    }

    @Override
    public Optional<UserDTO> getUserByEmail(String email) {
        System.out.println("Fetching user by email: " + email);
        return Optional.ofNullable(userRepository.findByEmail(email)
                .map(user -> modelMapper.map(user, UserDTO.class))
                .orElse(null));
    }
}
