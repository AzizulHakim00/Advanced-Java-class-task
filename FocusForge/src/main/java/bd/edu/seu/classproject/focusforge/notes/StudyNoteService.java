package bd.edu.seu.classproject.focusforge.notes;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudyNoteService {

    private final StudyNoteRepository studyNoteRepository;

    public StudyNoteService(StudyNoteRepository studyNoteRepository) {
        this.studyNoteRepository = studyNoteRepository;
    }

    public List<StudyNote> getAll(String ownerEmail) {
        return studyNoteRepository.findAllByOwnerEmailIgnoreCaseOrderByUpdatedAtDesc(ownerEmail);
    }

    public StudyNote save(StudyNote note, String ownerEmail) {
        note.setId(null);
        note.setOwnerEmail(ownerEmail);
        LocalDateTime now = LocalDateTime.now();
        note.setCreatedAt(now);
        note.setUpdatedAt(now);
        return studyNoteRepository.save(note);
    }

    public boolean delete(Long id, String ownerEmail) {
        return studyNoteRepository.findByIdAndOwnerEmailIgnoreCase(id, ownerEmail)
                .map(note -> {
                    studyNoteRepository.delete(note);
                    return true;
                }).orElse(false);
    }
}
