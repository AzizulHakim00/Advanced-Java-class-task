package bd.edu.seu.classproject.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class StudyCheckIn {

    @NotNull(message = "Available time is required")
    @Min(value = 10, message = "Available time must be at least 10 minutes")
    @Max(value = 600, message = "Available time cannot exceed 600 minutes")
    private Integer availableMinutes;

    @NotBlank(message = "Energy level is required")
    @Pattern(regexp = "Low|Medium|High", message = "Select a valid energy level")
    private String energyLevel;

    @NotBlank(message = "Mood is required")
    @Pattern(regexp = "Focused|Normal|Tired|Stressed", message = "Select a valid mood")
    private String mood;
}
