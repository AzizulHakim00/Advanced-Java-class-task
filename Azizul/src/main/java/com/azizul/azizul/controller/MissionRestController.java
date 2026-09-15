package com.azizul.azizul.controller;

import com.azizul.azizul.dto.MetricDTO;
import com.azizul.azizul.dto.MissionDTO;
import com.azizul.azizul.model.Mission;
import com.azizul.azizul.service.MissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/missions")
public class MissionRestController {

    private final MissionService missionService;


    // GET ALL MISSIONS
    @GetMapping
    public List<Mission> getMissions() {

        return missionService.getMissions();
    }


    // GET MISSION BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Mission> getMissionById(
            @PathVariable String id) {

        Mission mission =
                missionService.getMissionById(id);


        if (mission == null) {

            return ResponseEntity.notFound().build();
        }


        return ResponseEntity.ok(mission);
    }


    // CREATE MISSION
    @PostMapping
    public Mission createMission(
            @Valid @RequestBody MissionDTO missionDTO) {

        return missionService.createMission(missionDTO);
    }


    // UPDATE MISSION
    @PutMapping("/{id}")
    public ResponseEntity<Mission> updateMission(
            @PathVariable String id,
            @Valid @RequestBody MissionDTO missionDTO) {


        Mission mission = new Mission();


        mission.setName(missionDTO.name());

        mission.setAgency(missionDTO.agency());

        mission.setDate(missionDTO.date());

        mission.setOrbit(missionDTO.orbit());

        mission.setStatus(missionDTO.status());


        Mission updatedMission =
                missionService.updateMission(id, mission);


        if (updatedMission == null) {

            return ResponseEntity.notFound().build();
        }


        return ResponseEntity.ok(updatedMission);
    }


    // DELETE MISSION
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMission(
            @PathVariable String id) {


        Mission mission =
                missionService.getMissionById(id);


        if (mission == null) {

            return ResponseEntity.notFound().build();
        }


        missionService.deleteMission(id);


        return ResponseEntity.ok(
                "Mission deleted successfully."
        );
    }


    // SEARCH MISSION BY NAME
    @GetMapping("/search")
    public ResponseEntity<Mission> searchMission(
            @RequestParam String name) {


        Mission mission =
                missionService.findMissionByName(name);


        if (mission == null) {

            return ResponseEntity.notFound().build();
        }


        return ResponseEntity.ok(mission);
    }


    // GET METRICS
    @GetMapping("/metrics")
    public MetricDTO getMetrics() {

        return missionService.showMetric();
    }
}