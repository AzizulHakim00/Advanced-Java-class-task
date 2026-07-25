package bd.edu.seu.classproject;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/focusforge")
public class FocusForgeController {

    private final RecommendationService recommendationService;
    private final List<StudyTask> tasks = new ArrayList<>();

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
        long pendingCount = tasks.stream()
                .filter(task -> "Pending".equalsIgnoreCase(task.getStatus()))
                .count();

        long inProgressCount = tasks.stream()
                .filter(task -> "In Progress".equalsIgnoreCase(task.getStatus()))
                .count();

        long completedCount = tasks.stream()
                .filter(task -> "Completed".equalsIgnoreCase(task.getStatus()))
                .count();

        long urgentCount = tasks.stream()
                .filter(task -> !"Completed".equalsIgnoreCase(task.getStatus()))
                .filter(task -> !"Skipped".equalsIgnoreCase(task.getStatus()))
                .filter(task -> ChronoUnit.DAYS.between(LocalDate.now(), task.getDeadline()) <= 2)
                .count();

        long overdueCount = tasks.stream()
                .filter(StudyTask::isOverdue)
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

        model.addAttribute("totalCount", tasks.size());
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("inProgressCount", inProgressCount);
        model.addAttribute("completedCount", completedCount);
        model.addAttribute("urgentCount", urgentCount);
        model.addAttribute("overdueCount", overdueCount);
        model.addAttribute("productivityScore", productivityScore);
        model.addAttribute("upcomingTasks", upcomingTasks);

        return "dashboard";
    }

    @GetMapping("/tasks/add")
    public String showTaskForm(Model model) {
        StudyTask task = new StudyTask();
        task.setStatus("Pending");
        task.setDifficulty("Medium");
        task.setImportance("Medium");

        model.addAttribute("name", "Add Study Task");
        model.addAttribute("task", task);
        model.addAttribute("editMode", false);
        return "task-form";
    }

    @PostMapping("/tasks/add")
    public String addTask(@Valid @ModelAttribute("task") StudyTask task,
                          BindingResult bindingResult,
                          Model model) {
        boolean duplicateId = tasks.stream()
                .anyMatch(existing -> Objects.equals(existing.getTaskId(), task.getTaskId()));

        if (duplicateId) {
            bindingResult.rejectValue("taskId", "duplicate.taskId", "This Task ID already exists");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("name", "Add Study Task");
            model.addAttribute("editMode", false);
            return "task-form";
        }

        tasks.add(task);
        log.info("Study task added: {}", task);
        return "redirect:/focusforge/tasks";
    }

    @GetMapping("/tasks")
    public String showTaskList(@RequestParam(required = false) String keyword,
                               @RequestParam(required = false) String status,
                               @RequestParam(required = false) String importance,
                               Model model) {
        List<StudyTask> filteredTasks = tasks.stream()
                .filter(task -> {
                    boolean keywordMatch = keyword == null
                            || keyword.isBlank()
                            || task.getTaskName().toLowerCase().contains(keyword.toLowerCase())
                            || task.getCourseName().toLowerCase().contains(keyword.toLowerCase());

                    boolean statusMatch = status == null
                            || status.isBlank()
                            || task.getStatus().equalsIgnoreCase(status);

                    boolean importanceMatch = importance == null
                            || importance.isBlank()
                            || task.getImportance().equalsIgnoreCase(importance);

                    return keywordMatch && statusMatch && importanceMatch;
                })
                .sorted(Comparator.comparing(StudyTask::getDeadline))
                .toList();

        model.addAttribute("tasks", filteredTasks);
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedImportance", importance);
        return "task-list";
    }

    @GetMapping("/tasks/edit/{taskId}")
    public String showEditForm(@PathVariable Integer taskId, Model model) {
        StudyTask existingTask = findTask(taskId);
        if (existingTask == null) return "redirect:/focusforge/tasks";

        model.addAttribute("name", "Edit Study Task");
        model.addAttribute("task", existingTask);
        model.addAttribute("editMode", true);
        model.addAttribute("originalTaskId", taskId);
        return "task-form";
    }

    @PostMapping("/tasks/edit/{taskId}")
    public String updateTask(@PathVariable Integer taskId,
                             @Valid @ModelAttribute("task") StudyTask task,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("name", "Edit Study Task");
            model.addAttribute("editMode", true);
            model.addAttribute("originalTaskId", taskId);
            return "task-form";
        }

        for (int i = 0; i < tasks.size(); i++) {
            if (Objects.equals(tasks.get(i).getTaskId(), taskId)) {
                task.setTaskId(taskId);
                tasks.set(i, task);
                log.info("Study task updated: {}", task);
                break;
            }
        }
        return "redirect:/focusforge/tasks";
    }

    @GetMapping("/tasks/delete/{taskId}")
    public String deleteTask(@PathVariable Integer taskId) {
        tasks.removeIf(task -> Objects.equals(task.getTaskId(), taskId));
        log.info("Study task deleted. Task ID: {}", taskId);
        return "redirect:/focusforge/tasks";
    }

    @GetMapping("/tasks/status/{taskId}")
    public String updateTaskStatus(@PathVariable Integer taskId,
                                   @RequestParam String value) {
        StudyTask task = findTask(taskId);
        if (task != null && statuses().contains(value)) {
            task.setStatus(value);
            log.info("Study task status updated. Task ID: {}, Status: {}", taskId, value);
        }
        return "redirect:/focusforge/tasks";
    }

    @GetMapping("/tasks/complete/{taskId}")
    public String completeTask(@PathVariable Integer taskId) {
        StudyTask task = findTask(taskId);
        if (task != null) {
            task.setStatus("Completed");
            log.info("Study task completed. Task ID: {}", taskId);
        }
        return "redirect:/focusforge/tasks";
    }

    @GetMapping("/check-in")
    public String showCheckIn(Model model) {
        StudyCheckIn checkIn = new StudyCheckIn();
        checkIn.setAvailableMinutes(45);
        checkIn.setEnergyLevel("Medium");
        checkIn.setMood("Normal");
        model.addAttribute("checkIn", checkIn);
        return "check-in";
    }

    @PostMapping("/recommend")
    public String recommendTask(@Valid @ModelAttribute("checkIn") StudyCheckIn checkIn,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) return "check-in";

        RecommendationResult result = recommendationService.recommend(tasks, checkIn);
        model.addAttribute("checkIn", checkIn);
        model.addAttribute("result", result);
        model.addAttribute("hasRecommendation", result != null);
        return "recommendation";
    }

    @GetMapping("/recommendation/complete/{taskId}")
    public String completeRecommendedTask(@PathVariable Integer taskId) {
        return completeTask(taskId);
    }

    private StudyTask findTask(Integer taskId) {
        return tasks.stream()
                .filter(task -> Objects.equals(task.getTaskId(), taskId))
                .findFirst()
                .orElse(null);
    }
}
