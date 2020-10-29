package pjwstk.s16735.socialflashcardsapi.service;

import pjwstk.s16735.socialflashcardsapi.model.Card;
import pjwstk.s16735.socialflashcardsapi.model.Deck;

import java.util.List;

public interface DeckService {
    List<Deck> getAllDecks();
    Deck getDeckById(String id);
    Deck createDeck(Deck deck);
    Deck editDeck(Deck deck);
    void removeDeckById(String id);

    Deck addCard(String id, Card card);
    Deck editCard(String id, Card card);
    Deck removeCard(String id, Long cardId);
}
