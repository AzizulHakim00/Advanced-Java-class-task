package bd.edu.seu.classproject.focusforge.session;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/focusforge/sessions")
public class FocusSessionController {

    private final FocusSessionService focusSessionService;

    public FocusSessionController(FocusSessionService focusSessionService) {
        this.focusSessionService = focusSessionService;
    }

    @GetMapping
    public String sessions(Model model, Principal principal) {
        List<FocusSession> sessions = focusSessionService.getAll(principal.getName());
        FocusSession newSession = new FocusSession();
        newSession.setSessionDate(LocalDate.now());

        model.addAttribute("focusSession", newSession);
        model.addAttribute("sessions", sessions);
        model.addAttribute("totalMinutes", focusSessionService.totalMinutes(sessions));
        model.addAttribute("todayMinutes", focusSessionService.todayMinutes(sessions));
        return "focus-sessions";
    }

    @PostMapping
    public String add(@Valid @ModelAttribute("focusSession") FocusSession focusSession,
                      BindingResult bindingResult, Model model, Principal principal) {
        if (bindingResult.hasErrors()) {
            List<FocusSession> sessions = focusSessionService.getAll(principal.getName());
            model.addAttribute("sessions", sessions);
            model.addAttribute("totalMinutes", focusSessionService.totalMinutes(sessions));
            model.addAttribute("todayMinutes", focusSessionService.todayMinutes(sessions));
            return "focus-sessions";
        }

        focusSessionService.save(focusSession, principal.getName());
        return "redirect:/focusforge/sessions";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Principal principal) {
        focusSessionService.delete(id, principal.getName());
        return "redirect:/focusforge/sessions";
    }
}
