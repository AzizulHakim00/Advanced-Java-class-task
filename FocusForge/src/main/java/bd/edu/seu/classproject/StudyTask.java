package bd.edu.seu.classproject;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudyTask {

    @NotNull(message = "Task ID is required")
    @Min(value = 1, message = "Task ID must be at least 1")
    private Integer taskId;

    @NotBlank(message = "Task name cannot be blank")
    @Size(min = 3, max = 100, message = "Task name must be between 3 and 100 characters")
    private String taskName;

    @NotBlank(message = "Course name cannot be blank")
    @Size(min = 2, max = 60, message = "Course name must be between 2 and 60 characters")
    private String courseName;

    @NotNull(message = "Deadline is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;

    @NotNull(message = "Estimated time is required")
    @Min(value = 10, message = "Estimated time must be at least 10 minutes")
    @Max(value = 600, message = "Estimated time cannot exceed 600 minutes")
    private Integer estimatedMinutes;

    @NotBlank(message = "Difficulty is required")
    @Pattern(regexp = "Easy|Medium|Hard", message = "Select a valid difficulty")
    private String difficulty;

    @NotBlank(message = "Importance is required")
    @Pattern(regexp = "Low|Medium|High", message = "Select a valid importance")
    private String importance;

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "Pending|In Progress|Completed|Skipped", message = "Select a valid status")
    private String status;

    @Size(max = 300, message = "Description cannot exceed 300 characters")
    private String description;

    public long getDaysLeft() {
        if (deadline == null) {
            return Long.MAX_VALUE;
        }
        return ChronoUnit.DAYS.between(LocalDate.now(), deadline);
    }

    public boolean isOverdue() {
        return deadline != null
                && getDaysLeft() < 0
                && !"Completed".equalsIgnoreCase(status)
                && !"Skipped".equalsIgnoreCase(status);
    }

    public String getDeadlineLabel() {
        if (deadline == null) return "No deadline";
        if ("Completed".equalsIgnoreCase(status)) return "Completed";
        if ("Skipped".equalsIgnoreCase(status)) return "Skipped";

        long daysLeft = getDaysLeft();
        if (daysLeft < 0) {
            long overdueDays = Math.abs(daysLeft);
            return overdueDays + (overdueDays == 1 ? " day overdue" : " days overdue");
        }
        if (daysLeft == 0) return "Due today";
        if (daysLeft == 1) return "Due tomorrow";
        return daysLeft + " days left";
    }

    public String getDeadlineBadgeClass() {
        if ("Completed".equalsIgnoreCase(status)) return "badge-completed";
        if ("Skipped".equalsIgnoreCase(status)) return "badge-skipped";

        long daysLeft = getDaysLeft();
        if (daysLeft < 0) return "badge-overdue";
        if (daysLeft <= 2) return "badge-today";
        return "badge-upcoming";
    }
}
