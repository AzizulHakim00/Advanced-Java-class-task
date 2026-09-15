package com.safayet.afsos_nama.dto;

import com.safayet.afsos_nama.model.enums.AfsosCategory;
import com.safayet.afsos_nama.model.enums.AfsosLevel;
import com.safayet.afsos_nama.model.enums.AfsosStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class AfsosDTO {

    private Integer id;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title cannot be more than 100 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description cannot be more than 1000 characters")
    private String description;

    @NotNull(message = "Category is required")
    private AfsosCategory category;

    @NotNull(message = "Afsos level is required")
    private AfsosLevel level;

    @NotNull(message = "Regret date is required")
    @PastOrPresent(message = "Regret date cannot be in the future")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate regretDate;

    @Size(max = 100, message = "Course name cannot be more than 100 characters")
    private String relatedCourse;

    @Size(max = 50, message = "Semester cannot be more than 50 characters")
    private String semester;

    @Size(max = 500, message = "Consequence cannot be more than 500 characters")
    private String consequence;

    @Size(max = 500, message = "Lesson learned cannot be more than 500 characters")
    private String lessonLearned;

    @NotNull(message = "Status is required")
    private AfsosStatus status;

    @Size(max = 500, message = "Image URL cannot be more than 500 characters")
    private String imageUrl;

    private boolean sharedAnonymously;
}
