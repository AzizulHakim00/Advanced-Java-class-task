package com.safayet.afsos_nama.controller;

import com.safayet.afsos_nama.model.enums.ReactionType;
import com.safayet.afsos_nama.service.AfsosService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/confessions")
public class ConfessionController {

    private final AfsosService afsosService;

    @GetMapping
    public String feed(Model model) {
        model.addAttribute("confessions", afsosService.getApprovedConfessions());
        return "confessions/feed";
    }

    @PostMapping("/{id}/react/{reactionType}")
    public String react(
            @PathVariable Integer id,
            @PathVariable ReactionType reactionType
    ) {
        afsosService.react(id, reactionType);
        return "redirect:/confessions";
    }
}
