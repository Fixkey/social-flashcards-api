package pjwstk.s16735.socialflashcardsapi.service;

import pjwstk.s16735.socialflashcardsapi.model.Card;
import pjwstk.s16735.socialflashcardsapi.model.Deck;
import pjwstk.s16735.socialflashcardsapi.model.json.DeckExtended;

import java.util.List;

public interface DeckService {
    List<Deck> getAllDecks(String user);
    List<DeckExtended> getAllDecksBySubjectShallow(String subjectId, String user);
    Deck getDeckById(String id, String user);
    Deck createDeck(DeckExtended deck, String user);
    Deck editDeck(Deck deck, String user);
    void removeDeckById(String id, String user);

    Deck addCard(String id, Card card, String user);
    Deck editCard(String id, Card card, String user);
    Deck removeCard(String id, Long cardId, String user);

    void deleteAll();

    Deck getDeckByPermaLink(String permaLink, String secret, String user);

    Deck changeOwnership(Deck deck, String user);
}
