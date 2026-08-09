package bd.edu.seu.classproject.focusforge.users;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/sign-in")
    public String signIn() {
        return "sign-in";
    }

    @GetMapping("/sign-up")
    public String signUp(Model model) {
        model.addAttribute("user", new AppUser());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String register(@Valid @ModelAttribute("user") AppUser user,
                           BindingResult bindingResult) {
        if (userService.emailExists(user.getEmail())) {
            bindingResult.rejectValue("email", "error.email", "Email already exists");
        }

        if (bindingResult.hasErrors()) {
            return "sign-up";
        }

        userService.saveUser(user);
        return "redirect:/sign-in?registered=true";
    }
}
