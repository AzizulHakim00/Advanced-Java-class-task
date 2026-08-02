package bd.edu.seu.classproject.focusforge;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudyTaskService {

    private static final List<String> ALLOWED_STATUSES =
            List.of("Pending", "In Progress", "Completed", "Skipped");

    private static final Comparator<StudyTask> DEADLINE_COMPARATOR =
            Comparator.comparing(StudyTask::getDeadline,
                    Comparator.nullsLast(Comparator.naturalOrder()));

    private final StudyTaskRepository studyTaskRepository;

    public StudyTaskService(StudyTaskRepository studyTaskRepository) {
        this.studyTaskRepository = studyTaskRepository;
    }

    public List<StudyTask> getAllTasks() {
        return studyTaskRepository.findAll();
    }

    public Optional<StudyTask> getTaskById(Integer taskId) {
        if (taskId == null) return Optional.empty();
        return studyTaskRepository.findById(taskId);
    }

    public boolean existsById(Integer taskId) {
        return taskId != null && studyTaskRepository.existsById(taskId);
    }

    public StudyTask saveTask(StudyTask task) {
        Objects.requireNonNull(task, "Task cannot be null");
        updateCompletedDate(task);
        return studyTaskRepository.save(task);
    }

    public Optional<StudyTask> updateTask(Integer taskId, StudyTask submittedTask) {
        if (taskId == null || submittedTask == null) return Optional.empty();

        return studyTaskRepository.findById(taskId).map(existingTask -> {
            submittedTask.setTaskId(taskId);
            submittedTask.setCompletedDate(existingTask.getCompletedDate());
            updateCompletedDate(submittedTask);
            return studyTaskRepository.save(submittedTask);
        });
    }

    public boolean deleteTask(Integer taskId) {
        if (!existsById(taskId)) return false;
        studyTaskRepository.deleteById(taskId);
        return true;
    }

    public boolean updateStatus(Integer taskId, String requestedStatus) {
        String status = canonicalStatus(requestedStatus);
        if (status == null || taskId == null) return false;

        return studyTaskRepository.findById(taskId).map(task -> {
            task.setStatus(status);
            updateCompletedDate(task);
            studyTaskRepository.save(task);
            return true;
        }).orElse(false);
    }

    public List<StudyTask> searchTasks(String keyword, String status,
                                       String difficulty, String importance) {
        return getAllTasks().stream()
                .filter(Objects::nonNull)
                .filter(task -> matchesKeyword(task, keyword))
                .filter(task -> matches(task.getStatus(), status))
                .filter(task -> matches(task.getDifficulty(), difficulty))
                .filter(task -> matches(task.getImportance(), importance))
                .sorted(DEADLINE_COMPARATOR)
                .toList();
    }

    public long countByStatus(List<StudyTask> tasks, String status) {
        if (tasks == null || status == null) return 0;
        return tasks.stream()
                .filter(Objects::nonNull)
                .filter(task -> status.equalsIgnoreCase(task.getStatus()))
                .count();
    }

    public long countUrgent(List<StudyTask> tasks) {
        if (tasks == null) return 0;
        return tasks.stream()
                .filter(Objects::nonNull)
                .filter(this::isActive)
                .filter(task -> task.getDaysLeft() >= 0 && task.getDaysLeft() <= 2)
                .count();
    }

    public long countOverdue(List<StudyTask> tasks) {
        if (tasks == null) return 0;
        return tasks.stream()
                .filter(Objects::nonNull)
                .filter(StudyTask::isOverdue)
                .count();
    }

    public List<StudyTask> getUpcomingTasks(List<StudyTask> tasks, int limit) {
        if (tasks == null || limit <= 0) return List.of();
        return tasks.stream()
                .filter(Objects::nonNull)
                .filter(this::isActive)
                .sorted(DEADLINE_COMPARATOR)
                .limit(limit)
                .toList();
    }

    public int calculateProductivityScore(List<StudyTask> tasks) {
        if (tasks == null || tasks.isEmpty()) return 0;
        return percentage(countByStatus(tasks, "Completed"), tasks.size());
    }

    public List<StudyTask> getCompletedTasks() {
        return getAllTasks().stream()
                .filter(Objects::nonNull)
                .filter(task -> "Completed".equalsIgnoreCase(task.getStatus()))
                .sorted(Comparator.comparing(
                        StudyTask::getCompletedDate,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();
    }

    public int calculateTotalStudyMinutes(List<StudyTask> completedTasks) {
        if (completedTasks == null) return 0;
        return completedTasks.stream()
                .filter(Objects::nonNull)
                .mapToInt(task -> safeMinutes(task.getEstimatedMinutes()))
                .sum();
    }

    public int calculateCompletionRate(List<StudyTask> completedTasks) {
        long totalTasks = studyTaskRepository.count();
        long completedCount = completedTasks == null ? 0 : completedTasks.size();
        return percentage(completedCount, totalTasks);
    }

    public int calculateCurrentStreak(List<StudyTask> completedTasks) {
        if (completedTasks == null || completedTasks.isEmpty()) return 0;

        List<LocalDate> completedDates = completedTasks.stream()
                .filter(Objects::nonNull)
                .map(StudyTask::getCompletedDate)
                .filter(Objects::nonNull)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .toList();

        if (completedDates.isEmpty()) return 0;

        LocalDate expectedDate = LocalDate.now();
        if (!completedDates.contains(expectedDate)) {
            expectedDate = expectedDate.minusDays(1);
        }

        int streak = 0;
        for (LocalDate date : completedDates) {
            if (date.equals(expectedDate)) {
                streak++;
                expectedDate = expectedDate.minusDays(1);
            } else if (date.isBefore(expectedDate)) {
                break;
            }
        }
        return streak;
    }

    public int getMaximumStudyMinutes(List<StudyTask> completedTasks) {
        if (completedTasks == null || completedTasks.isEmpty()) return 1;
        int maximum = completedTasks.stream()
                .filter(Objects::nonNull)
                .mapToInt(task -> safeMinutes(task.getEstimatedMinutes()))
                .max()
                .orElse(0);
        return Math.max(1, maximum);
    }

    public int percentage(long value, long total) {
        if (total <= 0) return 0;
        return (int) Math.round((value * 100.0) / total);
    }

    private boolean matchesKeyword(StudyTask task, String keyword) {
        if (keyword == null || keyword.isBlank()) return true;

        String normalizedKeyword = keyword.trim().toLowerCase(Locale.ROOT);
        return String.valueOf(task.getTaskId()).toLowerCase(Locale.ROOT).contains(normalizedKeyword)
                || safeLower(task.getTaskName()).contains(normalizedKeyword)
                || safeLower(task.getCourseName()).contains(normalizedKeyword);
    }

    private boolean matches(String actualValue, String requestedValue) {
        return requestedValue == null
                || requestedValue.isBlank()
                || (actualValue != null && actualValue.equalsIgnoreCase(requestedValue.trim()));
    }

    private boolean isActive(StudyTask task) {
        return !"Completed".equalsIgnoreCase(task.getStatus())
                && !"Skipped".equalsIgnoreCase(task.getStatus());
    }

    private String canonicalStatus(String requestedStatus) {
        if (requestedStatus == null) return null;
        return ALLOWED_STATUSES.stream()
                .filter(status -> status.equalsIgnoreCase(requestedStatus.trim()))
                .findFirst()
                .orElse(null);
    }

    private void updateCompletedDate(StudyTask task) {
        if ("Completed".equalsIgnoreCase(task.getStatus())) {
            if (task.getCompletedDate() == null) {
                task.setCompletedDate(LocalDate.now());
            }
        } else {
            task.setCompletedDate(null);
        }
    }

    private int safeMinutes(Integer minutes) {
        return minutes == null ? 0 : Math.max(0, minutes);
    }

    private String safeLower(String value) {
        return value == null ? "" : value.toLowerCase(Locale.ROOT);
    }
}
