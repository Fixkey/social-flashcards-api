package pjwstk.s16735.socialflashcardsapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import pjwstk.s16735.socialflashcardsapi.model.Deck;

import java.util.List;

public interface DeckRepository extends MongoRepository<Deck, String> {
    Deck findDeckByPermaLinkEquals(String permaLink);
    Deck findByNameMatches(String regex);
    Deck findByPermaLinkMatches(String regex);
}
