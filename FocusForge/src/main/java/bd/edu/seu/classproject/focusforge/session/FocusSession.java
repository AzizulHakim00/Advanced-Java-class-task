package bd.edu.seu.classproject.focusforge.session;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "focus_sessions")
public class FocusSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "owner_email", nullable = false, length = 120)
    private String ownerEmail;

    @NotBlank(message = "Subject or task is required")
    @Size(max = 100, message = "Subject cannot exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String subject;

    @NotNull(message = "Duration is required")
    @Min(value = 5, message = "Duration must be at least 5 minutes")
    @Max(value = 600, message = "Duration cannot exceed 600 minutes")
    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @NotNull(message = "Session date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "session_date", nullable = false)
    private LocalDate sessionDate = LocalDate.now();

    @Size(max = 300, message = "Notes cannot exceed 300 characters")
    @Column(length = 300)
    private String notes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOwnerEmail() { return ownerEmail; }
    public void setOwnerEmail(String ownerEmail) { this.ownerEmail = ownerEmail; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
    public LocalDate getSessionDate() { return sessionDate; }
    public void setSessionDate(LocalDate sessionDate) { this.sessionDate = sessionDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
