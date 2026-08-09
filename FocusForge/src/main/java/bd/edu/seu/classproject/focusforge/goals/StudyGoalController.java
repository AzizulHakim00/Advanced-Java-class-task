package bd.edu.seu.classproject.focusforge.goals;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/goal")
public class StudyGoalController {

    private final StudyGoalService studyGoalService;

    @GetMapping("/add")
    public String showForm(Model model) {
        StudyGoal studyGoal = new StudyGoal();
        studyGoal.setStatus("In Progress");
        model.addAttribute("studyGoal", studyGoal);
        return "goal-form";
    }

    @PostMapping("/add")
    public String submit(@Valid @ModelAttribute StudyGoal studyGoal,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "goal-form";
        }
        studyGoalService.saveGoal(studyGoal);
        return "redirect:/goal/list";
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("goals", studyGoalService.getAll());
        return "goal-list";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam Long id) {
        studyGoalService.deleteById(id);
        return "redirect:/goal/list";
    }
}
