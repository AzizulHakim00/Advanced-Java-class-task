package com.safayet.afsos_nama.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProfileDTO {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot be more than 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    private String email;

    @Size(max = 150, message = "University cannot be more than 150 characters")
    private String university;

    @Size(max = 100, message = "Department cannot be more than 100 characters")
    private String department;

    @Size(max = 50, message = "Semester cannot be more than 50 characters")
    private String currentSemester;

    @DecimalMin(value = "0.00", message = "CGPA cannot be below 0")
    @DecimalMax(value = "4.00", message = "CGPA cannot be above 4")
    private BigDecimal currentCgpa;

    @DecimalMin(value = "0.00", message = "Target CGPA cannot be below 0")
    @DecimalMax(value = "4.00", message = "Target CGPA cannot be above 4")
    private BigDecimal targetCgpa;
}
