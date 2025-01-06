package com.rafael.rocha.spring_challenge.dto;

import com.rafael.rocha.spring_challenge.model.enums.UserType;
import lombok.Data;

import java.util.Date;

@Data
public class UserResponseDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private Date birthDate;
    private UserType userType;
}
