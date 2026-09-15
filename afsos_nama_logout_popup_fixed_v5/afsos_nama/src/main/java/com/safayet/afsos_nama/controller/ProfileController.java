package com.safayet.afsos_nama.controller;

import com.safayet.afsos_nama.dto.ProfileDTO;
import com.safayet.afsos_nama.model.User;
import com.safayet.afsos_nama.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {
        User user = userService.getUserByEmail(authentication.getName());
        model.addAttribute("profileDTO", userService.getProfile(user));
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(
            @Valid @ModelAttribute("profileDTO") ProfileDTO profileDTO,
            BindingResult bindingResult,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "profile";
        }

        User user = userService.getUserByEmail(authentication.getName());
        userService.updateProfile(user, profileDTO);
        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Profile updated successfully."
        );

        return "redirect:/profile";
    }
}
