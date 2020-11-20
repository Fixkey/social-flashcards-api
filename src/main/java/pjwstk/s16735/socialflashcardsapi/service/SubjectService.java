package pjwstk.s16735.socialflashcardsapi.service;

import pjwstk.s16735.socialflashcardsapi.model.Deck;
import pjwstk.s16735.socialflashcardsapi.model.Subject;

import java.util.List;

public interface SubjectService {
    List<String> getAllCategories();

    List<Subject> getAllSubjects(String category);

    Subject createSubject(Subject subject);

    Subject getSubjectById(String id);
}
