package bd.edu.seu.classproject.focusforge;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String redirectToFocusForge() {
        return "redirect:/focusforge/dashboard";
    }
}
