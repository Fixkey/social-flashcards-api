package pjwstk.s16735.socialflashcardsapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pjwstk.s16735.socialflashcardsapi.model.Subject;

import java.util.List;

public interface SubjectRepository extends MongoRepository<Subject, String> {
    List<Subject> getAllByCategoryEquals(String category);
}
