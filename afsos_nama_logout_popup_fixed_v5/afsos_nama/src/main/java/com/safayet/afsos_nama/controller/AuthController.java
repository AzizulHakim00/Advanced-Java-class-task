package com.safayet.afsos_nama.controller;

import com.safayet.afsos_nama.dto.RegistrationDTO;
import com.safayet.afsos_nama.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // Show login page
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    // Show registration page
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registrationDTO", new RegistrationDTO());
        return "auth/register";
    }

    // Process registration form
    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute RegistrationDTO registrationDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        // Check whether both passwords match
        if (!Objects.equals(
                registrationDTO.getPassword(),
                registrationDTO.getConfirmPassword()
        )) {
            result.rejectValue(
                    "confirmPassword",
                    "password.mismatch",
                    "Passwords do not match"
            );
        }
        if (result.hasErrors()) {
            return "auth/register";
        }

        try {
            // Save new user
            userService.register(registrationDTO);
        } catch (IllegalArgumentException exception) {
            // Show duplicate email error
            result.rejectValue(
                    "email",
                    "email.exists",
                    exception.getMessage()
            );

            return "auth/register";
        }

        // Message shown after redirecting
        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Registration successful. You can log in now."
        );

        return "redirect:/login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error/403";
    }
}