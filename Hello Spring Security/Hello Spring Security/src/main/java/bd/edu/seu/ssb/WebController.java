package bd.edu.seu.ssb;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String homePage() {
        return "home";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String role,
            @RequestParam String password) {

        User user = new User();
        user.setName(name);
        user.setEmail(email);

        // Password encrypt
        user.setPassword(passwordEncoder.encode(password));

        // Example role
        user.getRoles().add(role);

        userService.signup(user);

        return "redirect:/signin";
    }

    @GetMapping("/signin")
    public String signinPage() {
        return "signin";
    }

    @GetMapping("/user")
    public String userPage() {
        return "user";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "admin";
    }
    
//    @PostMapping("/signin")
//    public String signin(@RequestParam String email, @RequestParam String password) {
//
//        User user = userService.login(email, password);
//
//        if (user != null) {
//            return "home";
//        }
//
//        return "signin";
//    }
}