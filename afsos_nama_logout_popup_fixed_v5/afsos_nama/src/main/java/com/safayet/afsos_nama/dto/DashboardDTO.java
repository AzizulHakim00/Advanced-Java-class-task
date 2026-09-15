package com.safayet.afsos_nama.dto;

import com.safayet.afsos_nama.model.Afsos;
import com.safayet.afsos_nama.model.StudentPromise;

import java.util.List;
import java.util.Map;

public record DashboardDTO(
        long totalAfsos,
        long thisWeek,
        String mostCommonCategory,
        String highestLevel,
        int survivalScore,
        String survivalStatus,
        long activePromises,
        long brokenPromises,
        String repeatedWarning,
        List<Afsos> recentAfsos,
        List<StudentPromise> upcomingPromises,
        Map<String, Long> categoryBreakdown
) {
}
