package bd.edu.seu.classproject.focusforge.exams;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamScheduleService {

    private final ExamScheduleRepository examScheduleRepository;

    public ExamScheduleService(ExamScheduleRepository examScheduleRepository) {
        this.examScheduleRepository = examScheduleRepository;
    }

    public ExamSchedule saveExam(ExamSchedule examSchedule) {
        return examScheduleRepository.save(examSchedule);
    }

    public List<ExamSchedule> getAll() {
        return examScheduleRepository.findAll();
    }

    public void deleteById(Long id) {
        if (id != null && examScheduleRepository.existsById(id)) {
            examScheduleRepository.deleteById(id);
        }
    }
}
