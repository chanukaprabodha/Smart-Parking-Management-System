package com.example.userservice.dto;

import com.example.userservice.entity.UserRole;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private String id;
    private String name;
    private String email;
    private String password;
    private UserRole role;
    private Date createdAt;
}
