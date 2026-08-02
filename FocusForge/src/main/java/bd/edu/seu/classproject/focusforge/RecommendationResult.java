package bd.edu.seu.classproject.focusforge;

import java.util.List;

public class RecommendationResult {
    private final StudyTask task;
    private final int score;
    private final int matchPercentage;
    private final String matchLabel;
    private final List<String> reasons;

    public RecommendationResult(StudyTask task, int score, int matchPercentage,
                                String matchLabel, List<String> reasons) {
        this.task = task;
        this.score = score;
        this.matchPercentage = matchPercentage;
        this.matchLabel = matchLabel;
        this.reasons = List.copyOf(reasons);
    }

    public StudyTask getTask() { return task; }
    public int getScore() { return score; }
    public int getMatchPercentage() { return matchPercentage; }
    public String getMatchLabel() { return matchLabel; }
    public List<String> getReasons() { return reasons; }
}
