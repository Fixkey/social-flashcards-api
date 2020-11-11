package pjwstk.s16735.socialflashcardsapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pjwstk.s16735.socialflashcardsapi.model.ApplicationUser;

import java.util.List;

public interface ApplicationUserRepository extends MongoRepository<ApplicationUser, String> {
    ApplicationUser findByUsername(String username);
    ApplicationUser findByUsernameMatches(String regex);
    List<ApplicationUser> findAllByUsernameMatches(String regex);
}