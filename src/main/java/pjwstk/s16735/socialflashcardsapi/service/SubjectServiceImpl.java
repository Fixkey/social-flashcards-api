package pjwstk.s16735.socialflashcardsapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pjwstk.s16735.socialflashcardsapi.model.Subject;
import pjwstk.s16735.socialflashcardsapi.repository.SubjectRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SubjectServiceImpl implements SubjectService {
    private SubjectRepository subjectRepository;

    public SubjectServiceImpl(@Autowired SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public List<String> getAllCategories() {
        List<Subject> subjects = subjectRepository.findAll();
        Set<String> categories = new HashSet<>();
        subjects.forEach(subject -> {
            categories.add(subject.getCategory());
        });
        return new ArrayList<>(categories);
    }

    @Override
    public List<Subject> getAllSubjects(String category) {
        return subjectRepository.getAllByCategoryEquals(category);
    }

    @Override
    public Subject createSubject(Subject subject) {
        List<Subject> subjects = subjectRepository.findAll();
        subjects.forEach(sub -> {
            if (subject.equals(sub)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Subject must be unique");
            }
        });
        return subjectRepository.save(subject);
    }

    @Override
    public Subject getSubjectById(String id) {
        return subjectRepository.findById(id).orElse(null);
    }
}
