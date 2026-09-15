package com.safayet.afsos_nama.controller;

import com.safayet.afsos_nama.dto.AfsosDTO;
import com.safayet.afsos_nama.model.Afsos;
import com.safayet.afsos_nama.model.User;
import com.safayet.afsos_nama.model.enums.AfsosCategory;
import com.safayet.afsos_nama.model.enums.AfsosLevel;
import com.safayet.afsos_nama.model.enums.AfsosStatus;
import com.safayet.afsos_nama.service.AfsosService;
import com.safayet.afsos_nama.service.DashboardService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/afsos")
public class AfsosController {

    private final AfsosService afsosService;
    private final UserService userService;
    private final DashboardService dashboardService;

    @GetMapping
    public String list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) AfsosCategory category,
            @RequestParam(required = false) AfsosLevel level,
            Authentication authentication,
            Model model
    )
    {
        User user = currentUser(authentication);

        model.addAttribute(
                "afsosList",
                afsosService.searchAfsos(user, keyword, category, level)
        );
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("selectedLevel", level);
        addOptions(model);

        return "afsos/list";
    }

    @GetMapping("/new")
    public String createForm(Authentication authentication, Model model) {
        User user = currentUser(authentication);
        AfsosDTO dto = new AfsosDTO();

        dto.setRegretDate(LocalDate.now());
        dto.setStatus(AfsosStatus.NEW);
        dto.setSemester(user.getCurrentSemester());

        model.addAttribute("afsosDTO", dto);
        addOptions(model);

        return "afsos/form";
    }

    @PostMapping("/save")
    public String save(
            @Valid @ModelAttribute("afsosDTO") AfsosDTO afsosDTO,
            BindingResult bindingResult,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            addOptions(model);
            return "afsos/form";
        }

        afsosService.saveAfsos(afsosDTO, currentUser(authentication));

        redirectAttributes.addFlashAttribute(
                "successMessage",
                afsosDTO.getId() == null
                        ? "Afsos added successfully."
                        : "Afsos updated successfully."
        );

        return "redirect:/afsos";
    }

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable Integer id,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        Afsos afsos = afsosService.getAfsosById(id, currentUser(authentication));

        if (afsos == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Afsos not found.");
            return "redirect:/afsos";
        }

        AfsosDTO dto = new AfsosDTO();
        BeanUtils.copyProperties(afsos, dto);

        model.addAttribute("afsosDTO", dto);
        addOptions(model);

        return "afsos/form";
    }

    @GetMapping("/details/{id}")
    public String details(
            @PathVariable Integer id,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        Afsos afsos = afsosService.getAfsosById(id, currentUser(authentication));

        if (afsos == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Afsos not found.");
            return "redirect:/afsos";
        }

        model.addAttribute("afsos", afsos);
        return "afsos/details";
    }

    @PostMapping("/delete/{id}")
    public String delete(
            @PathVariable Integer id,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        afsosService.deleteAfsos(id, currentUser(authentication));
        redirectAttributes.addFlashAttribute("successMessage", "Afsos deleted.");
        return "redirect:/afsos";
    }

    @GetMapping("/timeline")
    public String timeline(Authentication authentication, Model model) {
        model.addAttribute(
                "afsosList",
                afsosService.getAllAfsos(currentUser(authentication))
        );
        return "afsos/timeline";
    }

    @GetMapping("/report")
    public String report(Authentication authentication, Model model) {
        User user = currentUser(authentication);

        model.addAttribute("reportData", dashboardService.buildDashboard(user));
        model.addAttribute("afsosList", afsosService.getAllAfsos(user));

        return "afsos/report";
    }

    private User currentUser(Authentication authentication) {
        return userService.getUserByEmail(authentication.getName());
    }

    private void addOptions(Model model) {
        model.addAttribute("categories", AfsosCategory.values());
        model.addAttribute("levels", AfsosLevel.values());
        model.addAttribute("statuses", AfsosStatus.values());
    }
}
