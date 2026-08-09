package bd.edu.seu.classproject.focusforge.resources;

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
@RequestMapping("/resource")
public class StudyResourceController {

    private final StudyResourceService studyResourceService;

    @GetMapping("/add")
    public String showForm(Model model) {
        model.addAttribute("studyResource", new StudyResource());
        return "resource-form";
    }

    @PostMapping("/add")
    public String submit(@Valid @ModelAttribute StudyResource studyResource,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "resource-form";
        }
        studyResourceService.saveResource(studyResource);
        return "redirect:/resource/list";
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("resources", studyResourceService.getAll());
        return "resource-list";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam Long id) {
        studyResourceService.deleteById(id);
        return "redirect:/resource/list";
    }
}
