package com.azizul.azizul.controller;

import com.azizul.azizul.dto.MetricDTO;
import com.azizul.azizul.dto.MissionDTO;
import com.azizul.azizul.model.Mission;
import com.azizul.azizul.service.MissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mission")
public class MissionController {

    private final MissionService missionService;


    // SHOW PAGE
    @GetMapping
    public String missionPage(
            @RequestParam(required = false) String search,
            Model model) {

        List<Mission> missions;

        if (search != null && !search.isBlank()) {

            Mission mission = missionService.findMissionByName(search);

            missions = mission != null
                    ? List.of(mission)
                    : Collections.emptyList();

        } else {

            missions = missionService.getMissions();
        }

        model.addAttribute("missions", missions);
        model.addAttribute("metrics", missionService.showMetric());
        model.addAttribute("search", search);

        return "mission";
    }


    // CREATE OR UPDATE
    @PostMapping("/save")
    public String saveMission(
            @RequestParam(required = false) String id,
            @Valid @ModelAttribute MissionDTO dto,
            RedirectAttributes redirectAttributes) {

        if (id == null || id.isBlank()) {

            missionService.createMission(dto);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Mission added successfully."
            );

        } else {

            Mission mission = Mission.builder()
                    .name(dto.name())
                    .agency(dto.agency())
                    .date(dto.date())
                    .orbit(dto.orbit())
                    .status(dto.status())
                    .build();

            missionService.updateMission(id, mission);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Mission updated successfully."
            );
        }

        return "redirect:/mission";
    }


    // DELETE
    @PostMapping("/delete/{id}")
    public String deleteMission(
            @PathVariable String id,
            RedirectAttributes redirectAttributes) {

        missionService.deleteMission(id);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Mission deleted successfully."
        );

        return "redirect:/mission";
    }
}