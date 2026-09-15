package com.azizul.azizul.service;

import com.azizul.azizul.model.Status;
import com.azizul.azizul.dto.MetricDTO;
import com.azizul.azizul.dto.MissionDTO;
import com.azizul.azizul.model.Mission;
import com.azizul.azizul.repository.MissionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepo missionRepo;


    // CREATE
    public Mission createMission(MissionDTO dto) {

        Mission mission = Mission.builder()
                .name(dto.name())
                .agency(dto.agency())
                .date(dto.date())
                .orbit(dto.orbit())
                .status(dto.status())
                .build();

        return missionRepo.save(mission);
    }


    // GET ALL
    public List<Mission> getMissions() {

        return missionRepo.findAll();
    }


    // GET BY ID
    public Mission getMissionById(String id) {

        return missionRepo.findById(id)
                .orElse(null);
    }


    // UPDATE - LAMBDA
    public Mission updateMission(String id, Mission mission) {

        return missionRepo.findById(id)
                .map(existingMission -> {

                    existingMission.setName(mission.getName());
                    existingMission.setAgency(mission.getAgency());
                    existingMission.setDate(mission.getDate());
                    existingMission.setOrbit(mission.getOrbit());
                    existingMission.setStatus(mission.getStatus());

                    return missionRepo.save(existingMission);

                })
                .orElse(null);
    }


    // DELETE - LAMBDA
    public void deleteMission(String id) {

        missionRepo.findById(id)
                .ifPresent(missionRepo::delete);
    }


    // SEARCH BY NAME - LAMBDA
    public Mission findMissionByName(String name) {

        return missionRepo.findAll()
                .stream()
                .filter(mission ->
                        mission.getName() != null &&
                                mission.getName().equalsIgnoreCase(name)
                )
                .findFirst()
                .orElse(null);
    }


    // TOTAL MISSIONS
    public int totalMissions() {

        return (int) missionRepo.count();
    }


    // ACTIVE MISSIONS - LAMBDA
    public int activeMissions() {

        return (int) missionRepo.findAll()
                .stream()
                .filter(mission ->
                        mission.getStatus() == Status.ACTIVE
                )
                .count();
    }


    // PLANNED MISSIONS - LAMBDA
    public int plannedMissions() {

        return (int) missionRepo.findAll()
                .stream()
                .filter(mission ->
                        mission.getStatus() == Status.PLANNED
                )
                .count();
    }


    // METRICS
    public MetricDTO showMetric() {

        return new MetricDTO(
                totalMissions(),
                activeMissions(),
                plannedMissions()
        );
    }
}