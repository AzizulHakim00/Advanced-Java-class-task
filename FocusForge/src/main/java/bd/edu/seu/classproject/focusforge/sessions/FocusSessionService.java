package bd.edu.seu.classproject.focusforge.sessions;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FocusSessionService {

    private final FocusSessionRepository focusSessionRepository;

    public void saveSession(FocusSession focusSession) {
        focusSessionRepository.save(focusSession);
    }

    public List<FocusSession> getAll() {
        return focusSessionRepository.findAll();
    }

    public void deleteById(Long id) {
        focusSessionRepository.deleteById(id);
    }
}
