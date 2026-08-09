package bd.edu.seu.classproject.focusforge.goals;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudyGoalService {

    private final StudyGoalRepository studyGoalRepository;

    public StudyGoalService(StudyGoalRepository studyGoalRepository) {
        this.studyGoalRepository = studyGoalRepository;
    }

    public StudyGoal saveGoal(StudyGoal studyGoal) {
        return studyGoalRepository.save(studyGoal);
    }

    public List<StudyGoal> getAll() {
        return studyGoalRepository.findAll();
    }

    public void deleteById(Long id) {
        if (id != null && studyGoalRepository.existsById(id)) {
            studyGoalRepository.deleteById(id);
        }
    }
}
