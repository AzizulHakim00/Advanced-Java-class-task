package bd.edu.seu.classproject.focusforge.notes;

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
@RequestMapping("/note")
public class StudyNoteController {

    private final StudyNoteService studyNoteService;

    @GetMapping("/add")
    public String showForm(Model model) {
        model.addAttribute("studyNote", new StudyNote());
        return "note-form";
    }

    @PostMapping("/add")
    public String submit(@Valid @ModelAttribute StudyNote studyNote,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "note-form";
        }

        studyNoteService.saveNote(studyNote);
        return "redirect:/note/list";
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("notes", studyNoteService.getAll());
        return "note-list";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam Long id) {
        studyNoteService.deleteById(id);
        return "redirect:/note/list";
    }
}
