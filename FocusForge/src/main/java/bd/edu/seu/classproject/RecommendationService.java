package bd.edu.seu.classproject;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class RecommendationService {

    public RecommendationResult recommend(List<StudyTask> tasks, StudyCheckIn checkIn) {
        return tasks.stream()
                .filter(task -> !"Completed".equalsIgnoreCase(task.getStatus()))
                .filter(task -> !"Skipped".equalsIgnoreCase(task.getStatus()))
                .filter(task -> task.getEstimatedMinutes() <= checkIn.getAvailableMinutes())
                .map(task -> createResult(task, checkIn))
                .max(Comparator.comparingInt(RecommendationResult::getScore))
                .orElse(null);
    }

    private RecommendationResult createResult(StudyTask task, StudyCheckIn checkIn) {
        int score = 0;
        List<String> reasons = new ArrayList<>();

        switch (task.getImportance()) {
            case "High" -> {
                score += 30;
                reasons.add("It has high academic importance.");
            }
            case "Medium" -> {
                score += 20;
                reasons.add("It has medium academic importance.");
            }
            default -> score += 10;
        }

        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), task.getDeadline());
        if (daysLeft < 0) {
            score += 50;
            reasons.add("Its deadline has already passed, so it needs immediate attention.");
        } else if (daysLeft == 0) {
            score += 45;
            reasons.add("Its deadline is today.");
        } else if (daysLeft == 1) {
            score += 40;
            reasons.add("Its deadline is tomorrow.");
        } else if (daysLeft <= 3) {
            score += 28;
            reasons.add("Its deadline is within three days.");
        } else if (daysLeft <= 7) {
            score += 15;
            reasons.add("Its deadline is within one week.");
        } else {
            score += 5;
        }

        int spareMinutes = checkIn.getAvailableMinutes() - task.getEstimatedMinutes();
        if (spareMinutes <= 15) {
            score += 18;
            reasons.add("It fits your available study time very well.");
        } else if (spareMinutes <= 45) {
            score += 10;
            reasons.add("It can be completed within your available time.");
        }

        score += energyScore(task, checkIn, reasons);
        score += moodScore(task, checkIn, reasons);

        if ("In Progress".equalsIgnoreCase(task.getStatus())) {
            score += 12;
            reasons.add("You have already started this task.");
        }

        int matchPercentage = normalizeScore(score);
        return new RecommendationResult(task, score, matchPercentage, createMatchLabel(matchPercentage), reasons);
    }

    private int normalizeScore(int score) {
        int maximumRuleScore = 147;
        int percentage = (int) Math.round((score * 100.0) / maximumRuleScore);
        return Math.max(0, Math.min(100, percentage));
    }

    private String createMatchLabel(int matchPercentage) {
        if (matchPercentage >= 80) return "Excellent Match";
        if (matchPercentage >= 65) return "Strong Match";
        if (matchPercentage >= 45) return "Good Match";
        return "Possible Match";
    }

    private int energyScore(StudyTask task, StudyCheckIn checkIn, List<String> reasons) {
        if ("High".equals(checkIn.getEnergyLevel())) {
            if ("Hard".equals(task.getDifficulty())) {
                reasons.add("Your high energy matches this difficult task.");
                return 22;
            }
            return 10;
        }

        if ("Low".equals(checkIn.getEnergyLevel())) {
            if ("Easy".equals(task.getDifficulty())) {
                reasons.add("The easy difficulty matches your current low energy.");
                return 22;
            }
            if ("Hard".equals(task.getDifficulty())) return -15;
            return 8;
        }

        if ("Medium".equals(task.getDifficulty())) {
            reasons.add("The task difficulty matches your medium energy level.");
            return 16;
        }
        return 10;
    }

    private int moodScore(StudyTask task, StudyCheckIn checkIn, List<String> reasons) {
        return switch (checkIn.getMood()) {
            case "Focused" -> {
                if ("Hard".equals(task.getDifficulty())) {
                    reasons.add("Your focused mood is suitable for challenging work.");
                    yield 15;
                }
                yield 8;
            }
            case "Tired" -> {
                if (task.getEstimatedMinutes() <= 30 || "Easy".equals(task.getDifficulty())) {
                    reasons.add("This is a manageable task for a tired mood.");
                    yield 15;
                }
                yield -8;
            }
            case "Stressed" -> {
                if (task.getEstimatedMinutes() <= 45) {
                    reasons.add("A shorter task can help you make progress without overload.");
                    yield 12;
                }
                yield -5;
            }
            default -> 8;
        };
    }
}
