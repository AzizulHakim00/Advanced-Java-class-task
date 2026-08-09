package bd.edu.seu.classproject.focusforge.session;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FocusSessionRepository extends JpaRepository<FocusSession, Long> {
    List<FocusSession> findAllByOwnerEmailIgnoreCaseOrderBySessionDateDescIdDesc(String ownerEmail);
    Optional<FocusSession> findByIdAndOwnerEmailIgnoreCase(Long id, String ownerEmail);
    long countByOwnerEmailIgnoreCaseAndSessionDate(String ownerEmail, LocalDate sessionDate);
}
