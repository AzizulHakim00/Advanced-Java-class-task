package bd.edu.seu.classproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RecommendationResult {
    private StudyTask task;
    private int score;
    private int matchPercentage;
    private String matchLabel;
    private List<String> reasons;
}
