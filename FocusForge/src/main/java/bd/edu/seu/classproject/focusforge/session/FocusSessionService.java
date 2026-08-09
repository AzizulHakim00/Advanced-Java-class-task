package bd.edu.seu.classproject.focusforge.session;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FocusSessionService {

    private final FocusSessionRepository focusSessionRepository;

    public FocusSessionService(FocusSessionRepository focusSessionRepository) {
        this.focusSessionRepository = focusSessionRepository;
    }

    public List<FocusSession> getAll(String ownerEmail) {
        return focusSessionRepository.findAllByOwnerEmailIgnoreCaseOrderBySessionDateDescIdDesc(ownerEmail);
    }

    public FocusSession save(FocusSession session, String ownerEmail) {
        session.setId(null);
        session.setOwnerEmail(ownerEmail);
        if (session.getSessionDate() == null) session.setSessionDate(LocalDate.now());
        session.setCreatedAt(LocalDateTime.now());
        return focusSessionRepository.save(session);
    }

    public boolean delete(Long id, String ownerEmail) {
        return focusSessionRepository.findByIdAndOwnerEmailIgnoreCase(id, ownerEmail)
                .map(session -> {
                    focusSessionRepository.delete(session);
                    return true;
                }).orElse(false);
    }

    public int totalMinutes(List<FocusSession> sessions) {
        if (sessions == null) return 0;
        return sessions.stream()
                .map(FocusSession::getDurationMinutes)
                .filter(minutes -> minutes != null && minutes > 0)
                .mapToInt(Integer::intValue)
                .sum();
    }

    public int todayMinutes(List<FocusSession> sessions) {
        if (sessions == null) return 0;
        LocalDate today = LocalDate.now();
        return sessions.stream()
                .filter(session -> today.equals(session.getSessionDate()))
                .map(FocusSession::getDurationMinutes)
                .filter(minutes -> minutes != null && minutes > 0)
                .mapToInt(Integer::intValue)
                .sum();
    }
}
