package bd.edu.seu.classproject.focusforge;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyTaskRepository extends JpaRepository<StudyTask, Integer> {
    List<StudyTask> findAllByOwnerEmailIgnoreCase(String ownerEmail);
    List<StudyTask> findAllByOwnerEmailIsNull();
    Optional<StudyTask> findByTaskIdAndOwnerEmailIgnoreCase(Integer taskId, String ownerEmail);
    long countByOwnerEmailIgnoreCase(String ownerEmail);
}
