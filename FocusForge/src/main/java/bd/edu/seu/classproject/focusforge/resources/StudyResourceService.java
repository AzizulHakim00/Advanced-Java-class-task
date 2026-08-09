package bd.edu.seu.classproject.focusforge.resources;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudyResourceService {

    private final StudyResourceRepository studyResourceRepository;

    public StudyResourceService(StudyResourceRepository studyResourceRepository) {
        this.studyResourceRepository = studyResourceRepository;
    }

    public StudyResource saveResource(StudyResource studyResource) {
        return studyResourceRepository.save(studyResource);
    }

    public List<StudyResource> getAll() {
        return studyResourceRepository.findAll();
    }

    public void deleteById(Long id) {
        if (id != null && studyResourceRepository.existsById(id)) {
            studyResourceRepository.deleteById(id);
        }
    }
}
