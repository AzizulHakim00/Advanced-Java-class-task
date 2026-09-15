package com.safayet.afsos_nama.controller;

import com.safayet.afsos_nama.model.User;
import com.safayet.afsos_nama.service.DashboardService;
import com.safayet.afsos_nama.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final UserService userService;
    private final DashboardService dashboardService;

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        User user = userService.getUserByEmail(authentication.getName());
        model.addAttribute("dashboard", dashboardService.buildDashboard(user));
        return "dashboard";
    }
}
