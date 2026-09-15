package com.azizul.azizul.dto;

import com.azizul.azizul.model.Orbit;
import com.azizul.azizul.model.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record MissionDTO(

        @NotBlank(message = "Mission name is required")
        String name,

        @NotBlank(message = "Agency is required")
        String agency,

        @NotNull(message = "Launch date is required")
        LocalDate date,

        @NotNull(message = "Orbit is required")
        Orbit orbit,

        @NotNull(message = "Status is required")
        Status status

) {
}