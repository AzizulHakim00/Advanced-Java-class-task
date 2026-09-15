package com.safayet.afsos_nama.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegistrationDTO {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot be more than 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    @Size(max = 150, message = "Email cannot be more than 150 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Please confirm your password")
    private String confirmPassword;

    @Size(max = 150, message = "University cannot be more than 150 characters")
    private String university;

    @Size(max = 100, message = "Department cannot be more than 100 characters")
    private String department;

    @Size(max = 50, message = "Semester cannot be more than 50 characters")
    private String currentSemester;
}
