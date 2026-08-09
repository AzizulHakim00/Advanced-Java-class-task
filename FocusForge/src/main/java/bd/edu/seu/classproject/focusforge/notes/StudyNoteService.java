package bd.edu.seu.classproject.focusforge.notes;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudyNoteService {

    private final StudyNoteRepository studyNoteRepository;

    public StudyNoteService(StudyNoteRepository studyNoteRepository) {
        this.studyNoteRepository = studyNoteRepository;
    }

    public StudyNote saveNote(StudyNote studyNote) {
        return studyNoteRepository.save(studyNote);
    }

    public List<StudyNote> getAll() {
        return studyNoteRepository.findAll();
    }

    public void deleteById(Long id) {
        if (id != null && studyNoteRepository.existsById(id)) {
            studyNoteRepository.deleteById(id);
        }
    }
}
