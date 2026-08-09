package bd.edu.seu.classproject.focusforge.goals;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyGoalService {

    private final StudyGoalRepository studyGoalRepository;

    public StudyGoal saveGoal(StudyGoal studyGoal) {
        return studyGoalRepository.save(studyGoal);
    }

    public List<StudyGoal> getAll() {
        return studyGoalRepository.findAll();
    }

    public void deleteById(Long id) {
        studyGoalRepository.deleteById(id);
    }
}
