package com.safayet.afsos_nama.service;

import com.safayet.afsos_nama.dto.AfsosDTO;
import com.safayet.afsos_nama.model.Afsos;
import com.safayet.afsos_nama.model.User;
import com.safayet.afsos_nama.model.enums.AfsosCategory;
import com.safayet.afsos_nama.model.enums.AfsosLevel;
import com.safayet.afsos_nama.model.enums.AfsosStatus;
import com.safayet.afsos_nama.model.enums.ReactionType;
import com.safayet.afsos_nama.repository.AfsosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AfsosService {

    private final AfsosRepository afsosRepository;

    public void saveAfsos(AfsosDTO dto, User user) {
        Afsos afsos = dto.getId() == null
                ? new Afsos()
                : getAfsosById(dto.getId(), user);

        if (afsos == null) {
            throw new IllegalArgumentException("Afsos not found");
        }

        boolean newAfsos = afsos.getId() == null;

        BeanUtils.copyProperties(
                dto,
                afsos,
                "id",
                "user",
                "approvedForFeed",
                "sameBhaiCount",
                "ripCgpaCount",
                "nextSemesterCount"
        );

        if (newAfsos) {
            afsos.setUser(user);
        }

        if (afsos.getStatus() == null) {
            afsos.setStatus(AfsosStatus.NEW);
        }

        if (afsos.getSemester() == null || afsos.getSemester().isBlank()) {
            afsos.setSemester(user.getCurrentSemester());
        }

        afsos.setApprovedForFeed(false);

        afsosRepository.save(afsos);
    }

    public List<Afsos> getAllAfsos(User user) {
        return afsosRepository.findByUserOrderByRegretDateDesc(user);
    }

    public List<Afsos> searchAfsos(
            User user,
            String keyword,
            AfsosCategory category,
            AfsosLevel level
    ) {
        String safeKeyword = keyword == null
                ? ""
                : keyword.trim().toLowerCase(Locale.ROOT);

        return getAllAfsos(user).stream()
                .filter(afsos -> safeKeyword.isBlank()
                        || afsos.getTitle().toLowerCase(Locale.ROOT).contains(safeKeyword)
                        || afsos.getDescription().toLowerCase(Locale.ROOT).contains(safeKeyword)
                        || containsIgnoreCase(afsos.getRelatedCourse(), safeKeyword))
                .filter(afsos -> category == null || afsos.getCategory() == category)
                .filter(afsos -> level == null || afsos.getLevel() == level)
                .toList();
    }

    public Afsos getAfsosById(Integer id, User user) {
        Afsos afsos = afsosRepository.findById(id).orElse(null);

        if (afsos == null || !afsos.getUser().getId().equals(user.getId())) {
            return null;
        }

        return afsos;
    }

    public void deleteAfsos(Integer id, User user) {
        Afsos afsos = getAfsosById(id, user);

        if (afsos != null) {
            afsosRepository.delete(afsos);
        }
    }

    public List<Afsos> getApprovedConfessions() {
        return afsosRepository
                .findBySharedAnonymouslyTrueAndApprovedForFeedTrueOrderByRegretDateDesc();
    }

    public List<Afsos> getPendingConfessions() {
        return afsosRepository
                .findBySharedAnonymouslyTrueAndApprovedForFeedFalseOrderByRegretDateDesc();
    }

    public void approveConfession(Integer id) {
        Afsos afsos = afsosRepository.findById(id).orElse(null);

        if (afsos != null && afsos.isSharedAnonymously()) {
            afsos.setApprovedForFeed(true);
            afsosRepository.save(afsos);
        }
    }

    public void hideConfession(Integer id) {
        Afsos afsos = afsosRepository.findById(id).orElse(null);

        if (afsos != null) {
            afsos.setApprovedForFeed(false);
            afsos.setSharedAnonymously(false);
            afsosRepository.save(afsos);
        }
    }

    public void react(Integer id, ReactionType reactionType) {
        Afsos afsos = afsosRepository.findById(id).orElse(null);

        if (afsos == null || !afsos.isApprovedForFeed()) {
            return;
        }

        switch (reactionType) {
            case SAME_BHAI -> afsos.setSameBhaiCount(afsos.getSameBhaiCount() + 1);
            case RIP_CGPA -> afsos.setRipCgpaCount(afsos.getRipCgpaCount() + 1);
            case NEXT_SEMESTER -> afsos.setNextSemesterCount(afsos.getNextSemesterCount() + 1);
        }

        afsosRepository.save(afsos);
    }

    public long countAllAfsos() {
        return afsosRepository.count();
    }

    private boolean containsIgnoreCase(String value, String keyword) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(keyword);
    }
}
