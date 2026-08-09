package bd.edu.seu.classproject.focusforge.auth;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("user", new AppUser());
        return "signup";
    }

    @PostMapping("/signup")
    public String register(@Valid @ModelAttribute("user") AppUser user,
                           BindingResult bindingResult) {
        if (userService.emailExists(user.getEmail())) {
            bindingResult.rejectValue("email", "duplicate.email", "An account with this email already exists");
        }

        if (!Objects.equals(user.getPassword(), user.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "password.mismatch", "Passwords do not match");
        }

        if (bindingResult.hasErrors()) {
            user.setPassword("");
            user.setConfirmPassword("");
            return "signup";
        }

        userService.register(user);
        return "redirect:/auth/login?registered=true";
    }
}
