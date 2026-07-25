package bd.edu.seu.classproject;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RecommendationResult {
    private StudyTask task;
    private int score;
    private List<String> reasons;
}
