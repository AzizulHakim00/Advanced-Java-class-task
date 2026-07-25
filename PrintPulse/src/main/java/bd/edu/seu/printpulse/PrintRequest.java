package bd.edu.seu.printpulse;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "print_requests")
public class PrintRequest {

    @Id
    @NotNull(message = "Request ID is required")
    @Min(value = 1001, message = "Request ID must be at least 1001")
    @Max(value = 9999, message = "Request ID must be at most 9999")
    @Column(name = "request_id")
    private Integer requestId;

    @NotBlank(message = "Student name cannot be blank")
    @Size(min = 2, max = 80, message = "Student name must be between 2 and 80 characters")
    @Column(name = "student_name", nullable = false, length = 80)
    private String studentName;

    @NotBlank(message = "Student ID cannot be blank")
    @Pattern(regexp = "[A-Za-z0-9-]{4,25}", message = "Use 4-25 letters, numbers or hyphens")
    @Column(name = "student_id", nullable = false, length = 25)
    private String studentId;

    @NotBlank(message = "Document name cannot be blank")
    @Size(min = 3, max = 120, message = "Document name must be between 3 and 120 characters")
    @Column(name = "document_name", nullable = false, length = 120)
    private String documentName;

    @NotNull(message = "Total pages are required")
    @Min(value = 1, message = "Total pages must be at least 1")
    @Max(value = 500, message = "Total pages cannot exceed 500")
    @Column(name = "total_pages", nullable = false)
    private Integer totalPages;

    @NotNull(message = "Number of copies is required")
    @Min(value = 1, message = "Copies must be at least 1")
    @Max(value = 50, message = "Copies cannot exceed 50")
    @Column(nullable = false)
    private Integer copies;

    @NotBlank(message = "Print type is required")
    @Pattern(regexp = "Black & White|Color", message = "Select a valid print type")
    @Column(name = "print_type", nullable = false, length = 25)
    private String printType;

    @NotBlank(message = "Paper size is required")
    @Pattern(regexp = "A4|A3|Letter", message = "Select a valid paper size")
    @Column(name = "paper_size", nullable = false, length = 15)
    private String paperSize;

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "Waiting|Printing|Ready|Collected|Cancelled", message = "Select a valid status")
    @Column(nullable = false, length = 20)
    private String status;

    @NotNull(message = "Request date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "request_date", nullable = false)
    private LocalDate requestDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "collected_date")
    private LocalDate collectedDate;

    @Size(max = 250, message = "Notes cannot exceed 250 characters")
    @Column(length = 250)
    private String notes;

    @Transient
    public int getTotalPrintedPages() {
        if (totalPages == null || copies == null) return 0;
        return totalPages * copies;
    }

    @Transient
    public double getPricePerPage() {
        double price = "Color".equalsIgnoreCase(printType) ? 10.0 : 2.0;

        if ("A3".equalsIgnoreCase(paperSize)) {
            price += 3.0;
        } else if ("Letter".equalsIgnoreCase(paperSize)) {
            price += 1.0;
        }

        return price;
    }

    @Transient
    public double getTotalCost() {
        return getTotalPrintedPages() * getPricePerPage();
    }

    @Transient
    public String getStatusClass() {
        if ("Printing".equalsIgnoreCase(status)) return "status-printing";
        if ("Ready".equalsIgnoreCase(status)) return "status-ready";
        if ("Collected".equalsIgnoreCase(status)) return "status-collected";
        if ("Cancelled".equalsIgnoreCase(status)) return "status-cancelled";
        return "status-waiting";
    }
}
