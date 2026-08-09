package bd.edu.seu.classproject.focusforge.notes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyNoteService {

    private final StudyNoteRepository studyNoteRepository;

    public void saveNote(StudyNote studyNote) {
        studyNoteRepository.save(studyNote);
    }

    public List<StudyNote> getAll() {
        return studyNoteRepository.findAll();
    }

    public void deleteById(Long id) {
        studyNoteRepository.deleteById(id);
    }
}
