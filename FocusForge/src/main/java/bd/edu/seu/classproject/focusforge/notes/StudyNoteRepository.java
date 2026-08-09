package bd.edu.seu.classproject.focusforge.notes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyNoteRepository extends JpaRepository<StudyNote, Long> {
    List<StudyNote> findAllByOwnerEmailIgnoreCaseOrderByUpdatedAtDesc(String ownerEmail);
    Optional<StudyNote> findByIdAndOwnerEmailIgnoreCase(Long id, String ownerEmail);
}
