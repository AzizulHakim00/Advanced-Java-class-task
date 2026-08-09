package bd.edu.seu.classproject.focusforge.resources;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyResourceService {

    private final StudyResourceRepository studyResourceRepository;

    public StudyResource saveResource(StudyResource studyResource) {
        return studyResourceRepository.save(studyResource);
    }

    public List<StudyResource> getAll() {
        return studyResourceRepository.findAll();
    }

    public void deleteById(Long id) {
        studyResourceRepository.deleteById(id);
    }
}
