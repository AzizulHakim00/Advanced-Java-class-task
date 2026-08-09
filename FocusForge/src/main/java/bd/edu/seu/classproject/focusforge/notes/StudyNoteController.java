package bd.edu.seu.classproject.focusforge.notes;

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

@Controller
@RequestMapping("/focusforge/notes")
public class StudyNoteController {

    private final StudyNoteService studyNoteService;

    public StudyNoteController(StudyNoteService studyNoteService) {
        this.studyNoteService = studyNoteService;
    }

    @GetMapping
    public String notes(Model model, Principal principal) {
        model.addAttribute("studyNote", new StudyNote());
        model.addAttribute("notes", studyNoteService.getAll(principal.getName()));
        return "study-notes";
    }

    @PostMapping
    public String add(@Valid @ModelAttribute("studyNote") StudyNote studyNote,
                      BindingResult bindingResult, Model model, Principal principal) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("notes", studyNoteService.getAll(principal.getName()));
            return "study-notes";
        }

        studyNoteService.save(studyNote, principal.getName());
        return "redirect:/focusforge/notes";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Principal principal) {
        studyNoteService.delete(id, principal.getName());
        return "redirect:/focusforge/notes";
    }
}
