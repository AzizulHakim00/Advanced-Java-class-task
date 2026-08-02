package bd.edu.seu.classproject.focusforge;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/focusforge")
public class FocusForgeController {

    private final StudyTaskService studyTaskService;
    private final RecommendationService recommendationService;

    public FocusForgeController(StudyTaskService studyTaskService,
                                RecommendationService recommendationService) {
        this.studyTaskService = studyTaskService;
        this.recommendationService = recommendationService;
    }

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
        List<StudyTask> tasks = studyTaskService.getAllTasks();

        long pendingCount = studyTaskService.countOpenByStatus(tasks, "Pending");
        long inProgressCount = studyTaskService.countOpenByStatus(tasks, "In Progress");
        long completedCount = studyTaskService.countByStatus(tasks, "Completed");
        long urgentCount = studyTaskService.countUrgent(tasks);
        long overdueCount = studyTaskService.countOverdue(tasks);

        RecommendationResult dashboardRecommendation = recommendationService.recommend(
                tasks, createDefaultCheckIn(90));

        long chartTotal = completedCount + pendingCount + inProgressCount + overdueCount;
        int completedPercent = studyTaskService.percentage(completedCount, chartTotal);
        int pendingPercent = studyTaskService.percentage(
                pendingCount + inProgressCount, chartTotal);
        int overduePercent = chartTotal == 0
                ? 0
                : Math.max(0, 100 - completedPercent - pendingPercent);

        model.addAttribute("today", LocalDate.now()
                .format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy")));
        model.addAttribute("totalCount", tasks.size());
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("inProgressCount", inProgressCount);
        model.addAttribute("completedCount", completedCount);
        model.addAttribute("urgentCount", urgentCount);
        model.addAttribute("overdueCount", overdueCount);
        model.addAttribute("productivityScore", studyTaskService.calculateProductivityScore(tasks));
        model.addAttribute("upcomingTasks", studyTaskService.getUpcomingTasks(tasks, 5));
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

        addTaskFormAttributes(model, "Add New Study Task", task, false, null);
        return "task-form";
    }

    @PostMapping("/tasks/add")
    public String addTask(@Valid @ModelAttribute("task") StudyTask task,
                          BindingResult bindingResult, Model model) {
        if (studyTaskService.existsById(task.getTaskId())) {
            bindingResult.rejectValue(
                    "taskId", "duplicate.taskId", "This Task ID already exists");
        }

        if (bindingResult.hasErrors()) {
            addTaskFormAttributes(model, "Add New Study Task", task, false, null);
            return "task-form";
        }

        studyTaskService.saveTask(task);
        return "redirect:/focusforge/tasks";
    }

    @GetMapping("/tasks")
    public String showTaskList(@RequestParam(required = false) String keyword,
                               @RequestParam(required = false) String status,
                               @RequestParam(required = false) String difficulty,
                               @RequestParam(required = false) String importance,
                               Model model) {
        model.addAttribute("tasks", studyTaskService.searchTasks(
                keyword, status, difficulty, importance));
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedDifficulty", difficulty);
        model.addAttribute("selectedImportance", importance);
        return "task-list";
    }

    @GetMapping("/tasks/edit/{taskId}")
    public String showEditForm(@PathVariable Integer taskId, Model model) {
        StudyTask existingTask = studyTaskService.getTaskById(taskId).orElse(null);
        if (existingTask == null) return "redirect:/focusforge/tasks";

        addTaskFormAttributes(model, "Edit Study Task", existingTask, true, taskId);
        return "task-form";
    }

    @PostMapping("/tasks/edit/{taskId}")
    public String updateTask(@PathVariable Integer taskId,
                             @Valid @ModelAttribute("task") StudyTask task,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            addTaskFormAttributes(model, "Edit Study Task", task, true, taskId);
            return "task-form";
        }

        if (studyTaskService.updateTask(taskId, task).isEmpty()) {
            return "redirect:/focusforge/tasks";
        }
        return "redirect:/focusforge/tasks";
    }

    @GetMapping("/tasks/delete/{taskId}")
    public String deleteTask(@PathVariable Integer taskId) {
        studyTaskService.deleteTask(taskId);
        return "redirect:/focusforge/tasks";
    }

    @GetMapping("/tasks/status/{taskId}")
    public String updateTaskStatus(@PathVariable Integer taskId,
                                   @RequestParam String value) {
        studyTaskService.updateStatus(taskId, value);
        return "redirect:/focusforge/tasks";
    }

    @GetMapping("/check-in")
    public String showCheckIn(Model model) {
        model.addAttribute("checkIn", createDefaultCheckIn(60));
        return "check-in";
    }

    @PostMapping("/recommend")
    public String recommend(@Valid @ModelAttribute("checkIn") StudyCheckIn checkIn,
                            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) return "check-in";

        RecommendationResult result = recommendationService.recommend(
                studyTaskService.getAllTasks(), checkIn);
        model.addAttribute("checkIn", checkIn);
        model.addAttribute("result", result);
        model.addAttribute("hasRecommendation", result != null);
        return "recommendation";
    }

    @GetMapping("/history")
    public String showHistory(Model model) {
        List<StudyTask> completedTasks = studyTaskService.getCompletedTasks();

        model.addAttribute("completedTasks", completedTasks);
        model.addAttribute("recentCompletedTasks", completedTasks.stream().limit(5).toList());
        model.addAttribute("totalStudyMinutes",
                studyTaskService.calculateTotalStudyMinutes(completedTasks));
        model.addAttribute("completionRate",
                studyTaskService.calculateCompletionRate(completedTasks));
        model.addAttribute("currentStreak",
                studyTaskService.calculateCurrentStreak(completedTasks));
        model.addAttribute("maximumMinutes",
                studyTaskService.getMaximumStudyMinutes(completedTasks));
        return "history";
    }

    private StudyCheckIn createDefaultCheckIn(int availableMinutes) {
        StudyCheckIn checkIn = new StudyCheckIn();
        checkIn.setAvailableMinutes(availableMinutes);
        checkIn.setEnergyLevel("Medium");
        checkIn.setMood("Normal");
        return checkIn;
    }

    private void addTaskFormAttributes(Model model, String title, StudyTask task,
                                       boolean editMode, Integer originalTaskId) {
        model.addAttribute("name", title);
        model.addAttribute("task", task);
        model.addAttribute("editMode", editMode);
        if (originalTaskId != null) {
            model.addAttribute("originalTaskId", originalTaskId);
        }
    }
}
