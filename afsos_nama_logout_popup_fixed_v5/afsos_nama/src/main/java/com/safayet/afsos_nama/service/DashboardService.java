package com.safayet.afsos_nama.service;

import com.safayet.afsos_nama.dto.DashboardDTO;
import com.safayet.afsos_nama.model.Afsos;
import com.safayet.afsos_nama.model.StudentPromise;
import com.safayet.afsos_nama.model.User;
import com.safayet.afsos_nama.model.enums.AfsosCategory;
import com.safayet.afsos_nama.model.enums.AfsosLevel;
import com.safayet.afsos_nama.model.enums.PromiseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final AfsosService afsosService;
    private final PromiseService promiseService;

    public DashboardDTO buildDashboard(User user) {

        List<Afsos> afsosList = afsosService.getAllAfsos(user);
        List<StudentPromise> promises = promiseService.getAllPromises(user);

        Map<AfsosCategory, Long> categoryCounts = getCategoryCounts(afsosList);

        return new DashboardDTO(
                afsosList.size(),
                countThisWeek(afsosList),
                getMostCommonCategory(categoryCounts),
                getHighestLevel(afsosList),
                calculateSurvivalScore(afsosList, promises),
                getSurvivalStatus(calculateSurvivalScore(afsosList, promises)),
                countPromises(promises, PromiseStatus.ACTIVE),
                countPromises(promises, PromiseStatus.BROKEN),
                getRepeatedWarning(categoryCounts),
                afsosList.stream().limit(5).toList(),
                promises.stream()
                        .filter(p -> p.getStatus() == PromiseStatus.ACTIVE)
                        .limit(4)
                        .toList(),
                getCategoryBreakdown(categoryCounts)
        );
    }

    private long countThisWeek(List<Afsos> afsosList) {
        LocalDate weekStart = LocalDate.now().minusDays(6);

        return afsosList.stream()
                .filter(a -> !a.getRegretDate().isBefore(weekStart))
                .count();
    }

    private Map<AfsosCategory, Long> getCategoryCounts(List<Afsos> afsosList) {
        return afsosList.stream()
                .collect(Collectors.groupingBy(
                        Afsos::getCategory,
                        Collectors.counting()
                ));
    }

    private String getMostCommonCategory(
            Map<AfsosCategory, Long> categoryCounts
    ) {
        return categoryCounts.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> entry.getKey().getLabel())
                .orElse("No data yet");
    }

    private String getHighestLevel(List<Afsos> afsosList) {
        return afsosList.stream()
                .map(Afsos::getLevel)
                .max(Comparator.comparingInt(AfsosLevel::getScore))
                .map(AfsosLevel::getLabel)
                .orElse("No data yet");
    }

    private long countPromises(
            List<StudentPromise> promises,
            PromiseStatus status
    ) {
        return promises.stream()
                .filter(promise -> promise.getStatus() == status)
                .count();
    }

    private int calculateSurvivalScore(
            List<Afsos> afsosList,
            List<StudentPromise> promises
    ) {
        int damage = afsosList.stream()
                .mapToInt(a -> a.getLevel().getScore() * 3)
                .sum();

        long brokenPromises = countPromises(
                promises,
                PromiseStatus.BROKEN
        );

        damage += (int) brokenPromises * 4;

        return Math.max(0, 100 - Math.min(100, damage));
    }

    private String getRepeatedWarning(
            Map<AfsosCategory, Long> categoryCounts
    ) {
        return categoryCounts.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .filter(entry -> entry.getValue() >= 3)
                .map(entry ->
                        "You repeated "
                                + entry.getKey().getLabel()
                                + " "
                                + entry.getValue()
                                + " times. This is no longer an accident."
                )
                .orElse("");
    }

    private Map<String, Long> getCategoryBreakdown(
            Map<AfsosCategory, Long> categoryCounts
    ) {
        return categoryCounts.entrySet()
                .stream()
                .sorted(
                        Map.Entry
                                .<AfsosCategory, Long>comparingByValue()
                                .reversed()
                )
                .collect(Collectors.toMap(
                        entry -> entry.getKey().getLabel(),
                        Map.Entry::getValue,
                        (first, second) -> first,
                        LinkedHashMap::new
                ));
    }

    private String getSurvivalStatus(int score) {
        if (score >= 85) return "Safe";
        if (score >= 65) return "Slightly damaged";
        if (score >= 45) return "Serious situation";
        if (score >= 25) return "Dua needed";

        return "Next semester preparation started";
    }
}