package bd.edu.seu.classproject.focusforge.goals;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class StudyGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Goal title is required")
    private String title;

    @NotNull(message = "Target date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate targetDate;

    @NotBlank(message = "Status is required")
    private String status;

    private String description;
}
