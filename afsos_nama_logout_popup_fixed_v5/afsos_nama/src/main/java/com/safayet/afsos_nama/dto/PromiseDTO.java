package com.safayet.afsos_nama.dto;

import com.safayet.afsos_nama.model.enums.PromiseStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class PromiseDTO {

    private Integer id;

    @NotBlank(message = "Promise title is required")
    @Size(max = 150, message = "Promise cannot be more than 150 characters")
    private String title;

    @NotNull(message = "Target date is required")
    @FutureOrPresent(message = "Target date cannot be in the past")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate targetDate;

    @NotNull(message = "Status is required")
    private PromiseStatus status;
}
