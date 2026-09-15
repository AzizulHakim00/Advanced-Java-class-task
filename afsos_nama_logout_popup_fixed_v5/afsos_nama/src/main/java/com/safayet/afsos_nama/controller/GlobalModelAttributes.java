package com.safayet.afsos_nama.controller;

import com.safayet.afsos_nama.model.User;
import com.safayet.afsos_nama.model.enums.Role;
import com.safayet.afsos_nama.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributes {

    private final UserService userService;

    @ModelAttribute
    public void addCurrentUser(Model model, Authentication authentication) {
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return;
        }

        User user = userService.getUserByEmail(authentication.getName());

        if (user != null) {
            model.addAttribute("currentUser", user);
            model.addAttribute("isAdmin", user.getRole() == Role.ADMIN);
        }
    }
}
