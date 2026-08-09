package bd.edu.seu.classproject.focusforge.exams;

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
@RequestMapping("/exam")
public class ExamScheduleController {

    private final ExamScheduleService examScheduleService;

    @GetMapping("/add")
    public String showForm(Model model) {
        model.addAttribute("examSchedule", new ExamSchedule());
        return "exam-form";
    }

    @PostMapping("/add")
    public String submit(@Valid @ModelAttribute ExamSchedule examSchedule,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "exam-form";
        }
        examScheduleService.saveExam(examSchedule);
        return "redirect:/exam/list";
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("exams", examScheduleService.getAll());
        return "exam-list";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam Long id) {
        examScheduleService.deleteById(id);
        return "redirect:/exam/list";
    }
}
