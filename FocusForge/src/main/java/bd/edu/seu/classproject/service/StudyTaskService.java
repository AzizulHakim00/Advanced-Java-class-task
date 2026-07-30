package bd.edu.seu.classproject.service;

import bd.edu.seu.classproject.model.StudyTask;
import bd.edu.seu.classproject.repository.StudyTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudyTaskService {

    private static final List<String> ALLOWED_STATUSES =
            List.of("Pending", "In Progress", "Completed", "Skipped");

    private final StudyTaskRepository studyTaskRepository;

    public List<StudyTask> getAllTasks() {
        return studyTaskRepository.findAll();
    }

    public Optional<StudyTask> getTaskById(Integer taskId) {
        return studyTaskRepository.findById(taskId);
    }

    public boolean existsById(Integer taskId) {
        return taskId != null && studyTaskRepository.existsById(taskId);
    }

    public StudyTask saveTask(StudyTask task) {
        updateCompletedDate(task);
        return studyTaskRepository.save(task);
    }

    public Optional<StudyTask> updateTask(Integer taskId, StudyTask submittedTask) {
        return studyTaskRepository.findById(taskId).map(existingTask -> {
            submittedTask.setTaskId(taskId);
            submittedTask.setCompletedDate(existingTask.getCompletedDate());
            updateCompletedDate(submittedTask);
            return studyTaskRepository.save(submittedTask);
        });
    }

    public void deleteTask(Integer taskId) {
        if (existsById(taskId)) {
            studyTaskRepository.deleteById(taskId);
        }
    }

    public boolean updateStatus(Integer taskId, String status) {
        if (!ALLOWED_STATUSES.contains(status)) {
            return false;
        }

        return studyTaskRepository.findById(taskId).map(task -> {
            task.setStatus(status);
            updateCompletedDate(task);
            studyTaskRepository.save(task);
            return true;
        }).orElse(false);
    }

    public List<StudyTask> searchTasks(
            String keyword,
            String status,
            String difficulty,
            String importance) {

        return getAllTasks().stream()
                .filter(task -> matchesKeyword(task, keyword))
                .filter(task -> matches(task.getStatus(), status))
                .filter(task -> matches(task.getDifficulty(), difficulty))
                .filter(task -> matches(task.getImportance(), importance))
                .sorted(Comparator.comparing(StudyTask::getDeadline))
                .toList();
    }

    public long countByStatus(List<StudyTask> tasks, String status) {
        return tasks.stream()
                .filter(task -> status.equalsIgnoreCase(task.getStatus()))
                .count();
    }

    public long countUrgent(List<StudyTask> tasks) {
        return tasks.stream()
                .filter(this::isActive)
                .filter(task -> task.getDaysLeft() <= 2)
                .count();
    }

    public long countOverdue(List<StudyTask> tasks) {
        return tasks.stream().filter(StudyTask::isOverdue).count();
    }

    public List<StudyTask> getUpcomingTasks(List<StudyTask> tasks, int limit) {
        return tasks.stream()
                .filter(this::isActive)
                .sorted(Comparator.comparing(StudyTask::getDeadline))
                .limit(limit)
                .toList();
    }

    public int calculateProductivityScore(List<StudyTask> tasks) {
        if (tasks.isEmpty()) return 0;
        long completed = countByStatus(tasks, "Completed");
        return percentage(completed, tasks.size());
    }

    public List<StudyTask> getCompletedTasks() {
        return getAllTasks().stream()
                .filter(task -> "Completed".equalsIgnoreCase(task.getStatus()))
                .sorted(Comparator.comparing(
                        StudyTask::getCompletedDate,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();
    }

    public int calculateTotalStudyMinutes(List<StudyTask> completedTasks) {
        return completedTasks.stream()
                .mapToInt(task -> task.getEstimatedMinutes() == null ? 0 : task.getEstimatedMinutes())
                .sum();
    }

    public int calculateCompletionRate(List<StudyTask> completedTasks) {
        long totalTasks = studyTaskRepository.count();
        return totalTasks == 0 ? 0 : percentage(completedTasks.size(), totalTasks);
    }

    public int calculateCurrentStreak(List<StudyTask> completedTasks) {
        List<LocalDate> completedDates = completedTasks.stream()
                .map(StudyTask::getCompletedDate)
                .filter(date -> date != null)
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
        return completedTasks.stream()
                .mapToInt(task -> task.getEstimatedMinutes() == null ? 0 : task.getEstimatedMinutes())
                .max()
                .orElse(1);
    }

    public int percentage(long value, long total) {
        if (total == 0) return 0;
        return (int) Math.round((value * 100.0) / total);
    }

    private boolean matchesKeyword(StudyTask task, String keyword) {
        if (keyword == null || keyword.isBlank()) return true;

        String normalizedKeyword = keyword.trim().toLowerCase();
        return String.valueOf(task.getTaskId()).contains(normalizedKeyword)
                || task.getTaskName().toLowerCase().contains(normalizedKeyword)
                || task.getCourseName().toLowerCase().contains(normalizedKeyword);
    }

    private boolean matches(String actualValue, String requestedValue) {
        return requestedValue == null
                || requestedValue.isBlank()
                || actualValue.equalsIgnoreCase(requestedValue);
    }

    private boolean isActive(StudyTask task) {
        return !"Completed".equalsIgnoreCase(task.getStatus())
                && !"Skipped".equalsIgnoreCase(task.getStatus());
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
}
