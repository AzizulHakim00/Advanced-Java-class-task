package bd.edu.seu.classproject.focusforge.sessions;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequestMapping("/session")
public class FocusSessionController {

    private final FocusSessionService focusSessionService;

    public FocusSessionController(FocusSessionService focusSessionService) {
        this.focusSessionService = focusSessionService;
    }

    @GetMapping("/add")
    public String showForm(Model model) {
        FocusSession focusSession = new FocusSession();
        focusSession.setSessionDate(LocalDate.now());
        model.addAttribute("focusSession", focusSession);
        return "session-form";
    }

    @PostMapping("/add")
    public String submit(@Valid @ModelAttribute("focusSession") FocusSession focusSession,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "session-form";
        focusSessionService.saveSession(focusSession);
        return "redirect:/session/list";
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("sessions", focusSessionService.getAll());
        return "session-list";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam Long id) {
        focusSessionService.deleteById(id);
        return "redirect:/session/list";
    }
}
