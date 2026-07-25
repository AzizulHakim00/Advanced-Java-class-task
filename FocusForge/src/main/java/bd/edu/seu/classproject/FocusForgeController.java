package bd.edu.seu.classproject;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/focusforge")
public class FocusForgeController {

    private final StudyTaskInterface studyTaskInterface;

    @ModelAttribute("difficulties")
    public List<String> difficulties() {
        return List.of("Easy", "Medium", "Hard");
    }

    @ModelAttribute("importanceLevels")
    public List<String> importanceLevels() {
        return List.of("Low", "Medium", "High");
    }

    @ModelAttribute("statuses")
    public List<String> statuses() {
        return List.of("Pending", "In Progress", "Completed", "Skipped");
    }

    @GetMapping
    public String home() {
        return "redirect:/focusforge/dashboard";
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {

        List<StudyTask> tasks = studyTaskInterface.findAll();

        long pendingCount = countStatus(tasks, "Pending");
        long inProgressCount = countStatus(tasks, "In Progress");
        long completedCount = countStatus(tasks, "Completed");
        long overdueCount = tasks.stream().filter(StudyTask::isOverdue).count();
        long urgentCount = tasks.stream()
                .filter(task -> !"Completed".equalsIgnoreCase(task.getStatus()))
                .filter(task -> !"Skipped".equalsIgnoreCase(task.getStatus()))
                .filter(task -> task.getDaysLeft() <= 2)
                .count();

        int productivityScore = tasks.isEmpty()
                ? 0
                : (int) Math.round((completedCount * 100.0) / tasks.size());

        List<StudyTask> upcomingTasks = tasks.stream()
                .filter(task -> !"Completed".equalsIgnoreCase(task.getStatus()))
                .filter(task -> !"Skipped".equalsIgnoreCase(task.getStatus()))
                .sorted(Comparator.comparing(StudyTask::getDeadline))
                .limit(5)
                .toList();

        StudyCheckIn defaultCheckIn = new StudyCheckIn();
        defaultCheckIn.setAvailableMinutes(90);
        defaultCheckIn.setEnergyLevel("Medium");
        defaultCheckIn.setMood("Normal");

        RecommendationResult dashboardRecommendation = recommendTask(tasks, defaultCheckIn);

        int completedPercent = percentage(completedCount, tasks.size());
        int pendingPercent = percentage(pendingCount + inProgressCount, tasks.size());
        int overduePercent = Math.max(0, 100 - completedPercent - pendingPercent);

        model.addAttribute("today", LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy")));
        model.addAttribute("totalCount", tasks.size());
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("inProgressCount", inProgressCount);
        model.addAttribute("completedCount", completedCount);
        model.addAttribute("urgentCount", urgentCount);
        model.addAttribute("overdueCount", overdueCount);
        model.addAttribute("productivityScore", productivityScore);
        model.addAttribute("upcomingTasks", upcomingTasks);
        model.addAttribute("dashboardRecommendation", dashboardRecommendation);
        model.addAttribute("completedPercent", completedPercent);
        model.addAttribute("pendingPercent", pendingPercent);
        model.addAttribute("overduePercent", overduePercent);

        return "dashboard";
    }

    @GetMapping("/tasks/add")
    public String showTaskForm(Model model) {

        StudyTask task = new StudyTask();
        task.setStatus("Pending");
        task.setDifficulty("Medium");
        task.setImportance("Medium");

        model.addAttribute("name", "Add New Study Task");
        model.addAttribute("task", task);
        model.addAttribute("editMode", false);

        return "task-form";
    }

    @PostMapping("/tasks/add")
    public String addTask(
            @Valid @ModelAttribute("task") StudyTask task,
            BindingResult bindingResult,
            Model model) {

        if (task.getTaskId() != null && studyTaskInterface.existsById(task.getTaskId())) {
            bindingResult.rejectValue("taskId", "duplicate.taskId", "This Task ID already exists");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("name", "Add New Study Task");
            model.addAttribute("editMode", false);
            return "task-form";
        }

        setCompletedDate(task);
        studyTaskInterface.save(task);
        log.info("Study task added: {}", task);

        return "redirect:/focusforge/tasks";
    }

    @GetMapping("/tasks")
    public String showTaskList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String importance,
            Model model) {

        List<StudyTask> filteredTasks = studyTaskInterface.findAll().stream()
                .filter(task -> {
                    boolean searchMatch = keyword == null
                            || keyword.isBlank()
                            || task.getTaskId().toString().contains(keyword)
                            || task.getTaskName().toLowerCase().contains(keyword.toLowerCase())
                            || task.getCourseName().toLowerCase().contains(keyword.toLowerCase());

                    boolean statusMatch = status == null
                            || status.isBlank()
                            || task.getStatus().equalsIgnoreCase(status);

                    boolean difficultyMatch = difficulty == null
                            || difficulty.isBlank()
                            || task.getDifficulty().equalsIgnoreCase(difficulty);

                    boolean importanceMatch = importance == null
                            || importance.isBlank()
                            || task.getImportance().equalsIgnoreCase(importance);

                    return searchMatch && statusMatch && difficultyMatch && importanceMatch;
                })
                .sorted(Comparator.comparing(StudyTask::getDeadline))
                .toList();

        model.addAttribute("tasks", filteredTasks);
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedDifficulty", difficulty);
        model.addAttribute("selectedImportance", importance);

        return "task-list";
    }

    @GetMapping("/tasks/edit/{taskId}")
    public String showEditForm(@PathVariable Integer taskId, Model model) {

        StudyTask existingTask = studyTaskInterface.findById(taskId).orElse(null);

        if (existingTask == null) {
            return "redirect:/focusforge/tasks";
        }

        model.addAttribute("name", "Edit Study Task");
        model.addAttribute("task", existingTask);
        model.addAttribute("editMode", true);
        model.addAttribute("originalTaskId", taskId);

        return "task-form";
    }

    @PostMapping("/tasks/edit/{taskId}")
    public String updateTask(
            @PathVariable Integer taskId,
            @Valid @ModelAttribute("task") StudyTask task,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("name", "Edit Study Task");
            model.addAttribute("editMode", true);
            model.addAttribute("originalTaskId", taskId);
            return "task-form";
        }

        StudyTask existingTask = studyTaskInterface.findById(taskId).orElse(null);

        if (existingTask == null) {
            return "redirect:/focusforge/tasks";
        }

        task.setTaskId(taskId);
        task.setCompletedDate(existingTask.getCompletedDate());
        setCompletedDate(task);

        studyTaskInterface.save(task);
        log.info("Study task updated: {}", task);

        return "redirect:/focusforge/tasks";
    }

    @GetMapping("/tasks/delete/{taskId}")
    public String deleteTask(@PathVariable Integer taskId) {

        if (studyTaskInterface.existsById(taskId)) {
            studyTaskInterface.deleteById(taskId);
            log.info("Study task deleted. ID: {}", taskId);
        }

        return "redirect:/focusforge/tasks";
    }

    @GetMapping("/tasks/status/{taskId}")
    public String updateTaskStatus(
            @PathVariable Integer taskId,
            @RequestParam String value) {

        StudyTask task = studyTaskInterface.findById(taskId).orElse(null);

        if (task != null && statuses().contains(value)) {
            task.setStatus(value);
            setCompletedDate(task);
            studyTaskInterface.save(task);
            log.info("Task status changed. ID: {}, Status: {}", taskId, value);
        }

        return "redirect:/focusforge/tasks";
    }

    @GetMapping("/check-in")
    public String showCheckIn(Model model) {

        StudyCheckIn checkIn = new StudyCheckIn();
        checkIn.setAvailableMinutes(60);
        checkIn.setEnergyLevel("Medium");
        checkIn.setMood("Normal");

        model.addAttribute("checkIn", checkIn);
        return "check-in";
    }

    @PostMapping("/recommend")
    public String recommend(
            @Valid @ModelAttribute("checkIn") StudyCheckIn checkIn,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "check-in";
        }

        RecommendationResult result = recommendTask(studyTaskInterface.findAll(), checkIn);

        model.addAttribute("checkIn", checkIn);
        model.addAttribute("result", result);
        model.addAttribute("hasRecommendation", result != null);

        return "recommendation";
    }

    @GetMapping("/history")
    public String showHistory(Model model) {

        List<StudyTask> completedTasks = studyTaskInterface.findAll().stream()
                .filter(task -> "Completed".equalsIgnoreCase(task.getStatus()))
                .sorted(Comparator.comparing(StudyTask::getCompletedDate,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();

        int totalStudyMinutes = completedTasks.stream()
                .mapToInt(task -> task.getEstimatedMinutes() == null ? 0 : task.getEstimatedMinutes())
                .sum();

        long totalTasks = studyTaskInterface.count();
        int completionRate = totalTasks == 0
                ? 0
                : (int) Math.round((completedTasks.size() * 100.0) / totalTasks);

        int currentStreak = calculateCurrentStreak(completedTasks);
        int maximumMinutes = completedTasks.stream()
                .mapToInt(task -> task.getEstimatedMinutes() == null ? 0 : task.getEstimatedMinutes())
                .max()
                .orElse(1);

        model.addAttribute("completedTasks", completedTasks);
        model.addAttribute("recentCompletedTasks", completedTasks.stream().limit(5).toList());
        model.addAttribute("totalStudyMinutes", totalStudyMinutes);
        model.addAttribute("completionRate", completionRate);
        model.addAttribute("currentStreak", currentStreak);
        model.addAttribute("maximumMinutes", maximumMinutes);

        return "history";
    }

    private long countStatus(List<StudyTask> tasks, String status) {
        return tasks.stream()
                .filter(task -> status.equalsIgnoreCase(task.getStatus()))
                .count();
    }

    private int percentage(long value, int total) {
        if (total == 0) return 0;
        return (int) Math.round((value * 100.0) / total);
    }

    private void setCompletedDate(StudyTask task) {
        if ("Completed".equalsIgnoreCase(task.getStatus())) {
            if (task.getCompletedDate() == null) {
                task.setCompletedDate(LocalDate.now());
            }
        } else {
            task.setCompletedDate(null);
        }
    }

    private int calculateCurrentStreak(List<StudyTask> completedTasks) {
        List<LocalDate> completedDates = completedTasks.stream()
                .map(StudyTask::getCompletedDate)
                .filter(date -> date != null)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .toList();

        if (completedDates.isEmpty()) return 0;

        LocalDate expectedDate = LocalDate.now();
        if (!completedDates.contains(expectedDate)) {
            expectedDate = LocalDate.now().minusDays(1);
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

    private RecommendationResult recommendTask(List<StudyTask> tasks, StudyCheckIn checkIn) {

        RecommendationResult bestResult = null;

        for (StudyTask task : tasks) {
            if ("Completed".equalsIgnoreCase(task.getStatus())
                    || "Skipped".equalsIgnoreCase(task.getStatus())
                    || task.getEstimatedMinutes() > checkIn.getAvailableMinutes()) {
                continue;
            }

            int score = 0;
            List<String> reasons = new ArrayList<>();

            if ("High".equals(task.getImportance())) {
                score += 30;
                reasons.add("This task has high academic importance.");
            } else if ("Medium".equals(task.getImportance())) {
                score += 20;
                reasons.add("This task has medium academic importance.");
            } else {
                score += 10;
            }

            if (task.getDaysLeft() < 0) {
                score += 50;
                reasons.add("The deadline has passed and needs immediate attention.");
            } else if (task.getDaysLeft() == 0) {
                score += 45;
                reasons.add("The deadline is today.");
            } else if (task.getDaysLeft() <= 3) {
                score += 30;
                reasons.add("The deadline is very close.");
            } else if (task.getDaysLeft() <= 7) {
                score += 15;
                reasons.add("The deadline is within one week.");
            }

            int spareMinutes = checkIn.getAvailableMinutes() - task.getEstimatedMinutes();
            if (spareMinutes <= 15) {
                score += 18;
                reasons.add("It fits your available time very well.");
            } else {
                score += 10;
                reasons.add("It can be completed within your available time.");
            }

            if ("High".equals(checkIn.getEnergyLevel()) && "Hard".equals(task.getDifficulty())) {
                score += 22;
                reasons.add("Your high energy is suitable for this hard task.");
            } else if ("Low".equals(checkIn.getEnergyLevel()) && "Easy".equals(task.getDifficulty())) {
                score += 22;
                reasons.add("This easy task matches your current low energy.");
            } else if ("Medium".equals(checkIn.getEnergyLevel()) && "Medium".equals(task.getDifficulty())) {
                score += 16;
                reasons.add("The task difficulty matches your medium energy.");
            } else {
                score += 8;
            }

            if ("Focused".equals(checkIn.getMood()) && "Hard".equals(task.getDifficulty())) {
                score += 15;
                reasons.add("Your focused mood is good for challenging work.");
            } else if ("Tired".equals(checkIn.getMood())
                    && ("Easy".equals(task.getDifficulty()) || task.getEstimatedMinutes() <= 30)) {
                score += 15;
                reasons.add("This task is manageable while you are tired.");
            } else if ("Stressed".equals(checkIn.getMood()) && task.getEstimatedMinutes() <= 45) {
                score += 12;
                reasons.add("A shorter task can help you progress without overload.");
            } else {
                score += 8;
            }

            if ("In Progress".equalsIgnoreCase(task.getStatus())) {
                score += 12;
                reasons.add("You have already started this task.");
            }

            int matchPercentage = Math.max(0,
                    Math.min(100, (int) Math.round((score * 100.0) / 147)));

            String matchLabel;
            if (matchPercentage >= 80) {
                matchLabel = "Excellent Match";
            } else if (matchPercentage >= 65) {
                matchLabel = "Strong Match";
            } else if (matchPercentage >= 45) {
                matchLabel = "Good Match";
            } else {
                matchLabel = "Possible Match";
            }

            RecommendationResult currentResult = new RecommendationResult(
                    task, score, matchPercentage, matchLabel, reasons);

            if (bestResult == null || currentResult.getScore() > bestResult.getScore()) {
                bestResult = currentResult;
            }
        }

        return bestResult;
    }
}
