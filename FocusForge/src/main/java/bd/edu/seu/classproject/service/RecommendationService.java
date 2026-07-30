package bd.edu.seu.classproject.service;

import bd.edu.seu.classproject.model.RecommendationResult;
import bd.edu.seu.classproject.model.StudyCheckIn;
import bd.edu.seu.classproject.model.StudyTask;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendationService {

    private static final int MAXIMUM_SCORE = 147;

    public RecommendationResult recommend(List<StudyTask> tasks, StudyCheckIn checkIn) {
        RecommendationResult bestResult = null;

        for (StudyTask task : tasks) {
            if (!isEligible(task, checkIn)) {
                continue;
            }

            List<String> reasons = new ArrayList<>();
            int score = importanceScore(task, reasons)
                    + deadlineScore(task, reasons)
                    + timeFitScore(task, checkIn, reasons)
                    + energyScore(task, checkIn, reasons)
                    + moodScore(task, checkIn, reasons)
                    + progressScore(task, reasons);

            int matchPercentage = Math.max(
                    0,
                    Math.min(100, (int) Math.round((score * 100.0) / MAXIMUM_SCORE)));

            RecommendationResult currentResult = new RecommendationResult(
                    task,
                    score,
                    matchPercentage,
                    matchLabel(matchPercentage),
                    reasons);

            if (bestResult == null || currentResult.getScore() > bestResult.getScore()) {
                bestResult = currentResult;
            }
        }

        return bestResult;
    }

    private boolean isEligible(StudyTask task, StudyCheckIn checkIn) {
        return !"Completed".equalsIgnoreCase(task.getStatus())
                && !"Skipped".equalsIgnoreCase(task.getStatus())
                && task.getEstimatedMinutes() != null
                && checkIn.getAvailableMinutes() != null
                && task.getEstimatedMinutes() <= checkIn.getAvailableMinutes();
    }

    private int importanceScore(StudyTask task, List<String> reasons) {
        if ("High".equalsIgnoreCase(task.getImportance())) {
            reasons.add("This task has high academic importance.");
            return 30;
        }
        if ("Medium".equalsIgnoreCase(task.getImportance())) {
            reasons.add("This task has medium academic importance.");
            return 20;
        }
        return 10;
    }

    private int deadlineScore(StudyTask task, List<String> reasons) {
        long daysLeft = task.getDaysLeft();
        if (daysLeft < 0) {
            reasons.add("The deadline has passed and needs immediate attention.");
            return 50;
        }
        if (daysLeft == 0) {
            reasons.add("The deadline is today.");
            return 45;
        }
        if (daysLeft <= 3) {
            reasons.add("The deadline is very close.");
            return 30;
        }
        if (daysLeft <= 7) {
            reasons.add("The deadline is within one week.");
            return 15;
        }
        return 0;
    }

    private int timeFitScore(StudyTask task, StudyCheckIn checkIn, List<String> reasons) {
        int spareMinutes = checkIn.getAvailableMinutes() - task.getEstimatedMinutes();
        if (spareMinutes <= 15) {
            reasons.add("It fits your available time very well.");
            return 18;
        }
        reasons.add("It can be completed within your available time.");
        return 10;
    }

    private int energyScore(StudyTask task, StudyCheckIn checkIn, List<String> reasons) {
        if ("High".equalsIgnoreCase(checkIn.getEnergyLevel())
                && "Hard".equalsIgnoreCase(task.getDifficulty())) {
            reasons.add("Your high energy is suitable for this hard task.");
            return 22;
        }
        if ("Low".equalsIgnoreCase(checkIn.getEnergyLevel())
                && "Easy".equalsIgnoreCase(task.getDifficulty())) {
            reasons.add("This easy task matches your current low energy.");
            return 22;
        }
        if ("Medium".equalsIgnoreCase(checkIn.getEnergyLevel())
                && "Medium".equalsIgnoreCase(task.getDifficulty())) {
            reasons.add("The task difficulty matches your medium energy.");
            return 16;
        }
        return 8;
    }

    private int moodScore(StudyTask task, StudyCheckIn checkIn, List<String> reasons) {
        if ("Focused".equalsIgnoreCase(checkIn.getMood())
                && "Hard".equalsIgnoreCase(task.getDifficulty())) {
            reasons.add("Your focused mood is good for challenging work.");
            return 15;
        }
        if ("Tired".equalsIgnoreCase(checkIn.getMood())
                && ("Easy".equalsIgnoreCase(task.getDifficulty())
                || task.getEstimatedMinutes() <= 30)) {
            reasons.add("This task is manageable while you are tired.");
            return 15;
        }
        if ("Stressed".equalsIgnoreCase(checkIn.getMood())
                && task.getEstimatedMinutes() <= 45) {
            reasons.add("A shorter task can help you progress without overload.");
            return 12;
        }
        return 8;
    }

    private int progressScore(StudyTask task, List<String> reasons) {
        if ("In Progress".equalsIgnoreCase(task.getStatus())) {
            reasons.add("You have already started this task.");
            return 12;
        }
        return 0;
    }

    private String matchLabel(int matchPercentage) {
        if (matchPercentage >= 80) return "Excellent Match";
        if (matchPercentage >= 65) return "Strong Match";
        if (matchPercentage >= 45) return "Good Match";
        return "Possible Match";
    }
}
