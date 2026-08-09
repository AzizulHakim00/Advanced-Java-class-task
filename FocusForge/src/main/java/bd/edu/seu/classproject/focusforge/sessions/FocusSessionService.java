package bd.edu.seu.classproject.focusforge.sessions;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FocusSessionService {

    private final FocusSessionRepository focusSessionRepository;

    public FocusSessionService(FocusSessionRepository focusSessionRepository) {
        this.focusSessionRepository = focusSessionRepository;
    }

    public FocusSession saveSession(FocusSession focusSession) {
        return focusSessionRepository.save(focusSession);
    }

    public List<FocusSession> getAll() {
        return focusSessionRepository.findAll();
    }

    public void deleteById(Long id) {
        if (id != null && focusSessionRepository.existsById(id)) {
            focusSessionRepository.deleteById(id);
        }
    }
}
