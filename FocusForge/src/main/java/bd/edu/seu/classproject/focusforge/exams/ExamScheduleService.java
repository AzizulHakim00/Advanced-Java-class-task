package bd.edu.seu.classproject.focusforge.exams;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamScheduleService {

    private final ExamScheduleRepository examScheduleRepository;

    public ExamSchedule saveExam(ExamSchedule examSchedule) {
        return examScheduleRepository.save(examSchedule);
    }

    public List<ExamSchedule> getAll() {
        return examScheduleRepository.findAll();
    }

    public void deleteById(Long id) {
        examScheduleRepository.deleteById(id);
    }
}
