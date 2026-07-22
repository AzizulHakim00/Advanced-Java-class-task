package bd.edu.seu.classproject;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Weather {

    @NotBlank(message = "Day cannot be blank")
    private String day;

    @NotNull(message = "Moisture is required")
    @Min(value = 0, message = "Moisture cannot be less than 0")
    @Max(value = 100, message = "Moisture cannot be more than 100")
    private Integer moisture;

    @NotNull(message = "Temperature is required")
    @DecimalMin(value = "-100.0", message = "Temperature must be at least -100")
    @DecimalMax(value = "100.0", message = "Temperature must be at most 100")
    private Double temperature;

    @NotNull(message = "UV index is required")
    @DecimalMin(value = "0.0", message = "UV index cannot be negative")
    @DecimalMax(value = "20.0", message = "UV index must be at most 20")
    private Double uvIndex;

    @NotNull(message = "Wind speed is required")
    @PositiveOrZero(message = "Wind speed cannot be negative")
    private Integer wind;
}