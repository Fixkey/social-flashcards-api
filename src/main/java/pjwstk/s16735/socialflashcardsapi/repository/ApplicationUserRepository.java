package pjwstk.s16735.socialflashcardsapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pjwstk.s16735.socialflashcardsapi.model.ApplicationUser;

public interface ApplicationUserRepository extends MongoRepository<ApplicationUser, String> {
    ApplicationUser findByUsername(String username);
}