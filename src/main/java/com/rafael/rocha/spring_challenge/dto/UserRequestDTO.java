package com.rafael.rocha.spring_challenge.dto;

import com.rafael.rocha.spring_challenge.model.enums.UserType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Date;

@Data
public class UserRequestDTO {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\+[0-9][0-9 -]{8,}$",
            message = "Phone number must match the international pattern (e.g: +55 11 98765-4321)")
    private String phone;

    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be in the past")
    private Date birthDate;

    @NotNull(message = "User type is required")
    private UserType userType;
}
