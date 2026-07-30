package bd.edu.seu.classproject.repository;

import bd.edu.seu.classproject.model.StudyTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyTaskRepository extends JpaRepository<StudyTask, Integer> {
}
