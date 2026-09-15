package com.safayet.afsos_nama.controller;

import com.safayet.afsos_nama.dto.PromiseDTO;
import com.safayet.afsos_nama.model.StudentPromise;
import com.safayet.afsos_nama.model.User;
import com.safayet.afsos_nama.model.enums.PromiseStatus;
import com.safayet.afsos_nama.service.PromiseService;
import com.safayet.afsos_nama.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/promises")
public class PromiseController {

    private final PromiseService promiseService;
    private final UserService userService;

    @GetMapping
    public String list(Authentication authentication, Model model) {
        model.addAttribute(
                "promises",
                promiseService.getAllPromises(currentUser(authentication))
        );
        return "promises/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        PromiseDTO dto = new PromiseDTO();
        dto.setTargetDate(LocalDate.now().plusDays(7));
        dto.setStatus(PromiseStatus.ACTIVE);

        model.addAttribute("promiseDTO", dto);
        model.addAttribute("statuses", PromiseStatus.values());

        return "promises/form";
    }

    @PostMapping("/save")
    public String save(
            @Valid @ModelAttribute("promiseDTO") PromiseDTO promiseDTO,
            BindingResult bindingResult,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("statuses", PromiseStatus.values());
            return "promises/form";
        }

        promiseService.savePromise(promiseDTO, currentUser(authentication));
        redirectAttributes.addFlashAttribute("successMessage", "Promise saved.");
        return "redirect:/promises";
    }

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable Integer id,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        StudentPromise promise = promiseService.getPromiseById(
                id,
                currentUser(authentication)
        );

        if (promise == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Promise not found.");
            return "redirect:/promises";
        }

        PromiseDTO dto = new PromiseDTO();
        BeanUtils.copyProperties(promise, dto);

        model.addAttribute("promiseDTO", dto);
        model.addAttribute("statuses", PromiseStatus.values());

        return "promises/form";
    }

    @PostMapping("/complete/{id}")
    public String complete(
            @PathVariable Integer id,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        promiseService.markCompleted(id, currentUser(authentication));
        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Promise completed. Character development unlocked."
        );
        return "redirect:/promises";
    }

    @PostMapping("/break/{id}")
    public String breakPromise(
            @PathVariable Integer id,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        promiseService.markBroken(id, currentUser(authentication));
        redirectAttributes.addFlashAttribute(
                "errorMessage",
                "Promise marked as broken. Kal theke abar try korben."
        );
        return "redirect:/promises";
    }

    @PostMapping("/delete/{id}")
    public String delete(
            @PathVariable Integer id,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        promiseService.deletePromise(id, currentUser(authentication));
        redirectAttributes.addFlashAttribute("successMessage", "Promise deleted.");
        return "redirect:/promises";
    }

    private User currentUser(Authentication authentication) {
        return userService.getUserByEmail(authentication.getName());
    }
}
