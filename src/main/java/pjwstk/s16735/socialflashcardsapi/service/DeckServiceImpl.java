package pjwstk.s16735.socialflashcardsapi.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pjwstk.s16735.socialflashcardsapi.model.Card;
import pjwstk.s16735.socialflashcardsapi.model.Deck;
import pjwstk.s16735.socialflashcardsapi.repository.DeckRepository;

import java.util.List;

@Service
public class DeckServiceImpl implements DeckService {
    private DeckRepository deckRepository;
    private MongoClient mongoClient;

    public DeckServiceImpl(@Autowired DeckRepository deckRepository, @Autowired MongoClient mongoClient) {
        this.deckRepository = deckRepository;
        this.mongoClient = mongoClient;
        MongoDatabase database = mongoClient.getDatabase("social-flashcards");
        var collectionT = database.getCollection("deck");
        var docs = collectionT.countDocuments();
    }

    @Override
    public List<Deck> getAllDecks() {
        return deckRepository.findAll();
    }

    @Override
    public Deck getDeckById(String id) {
//        Deck deck = deckRepository.findById(id).orElseThrow(() -> {throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Deck not found");});
        Deck deck = findDeckById(id);
        return deck;
    }

    @Override
    public Deck getDeckByPermaLink(String permaLink) {
        Deck deck = deckRepository.findDeckByPermaLinkEquals(permaLink);
        if (deck == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Deck " + permaLink + " doesn't exist");
        }
        return deckRepository.findDeckByPermaLinkEquals(permaLink);
    }

    @Override
    public Deck createDeck(Deck deck) {
        if (deck.getPermaLink() == null) {
            deck.setPermaLink(createPermaLinkFromName(deck.getName()));
        }
        Deck savedDeck = deckRepository.insert(deck);
        return savedDeck;
    }

    @Override
    public Deck editDeck(Deck deck) {
        findDeckById(deck.getId());
        deckRepository.save(deck);
        return deck;
    }

    @Override
    public void removeDeckById(String id) {
        deckRepository.deleteById(id);
    }

    @Override
    public Deck addCard(String id, Card card) {
//        Deck deck = deckRepository.findById(id).orElseThrow(() -> {throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deck doesn't exist");});
        Deck deck = findDeckById(id);
        Long maxId = -1L;
        for (Card c: deck.getCards()) {
            if (c.getId() != null && maxId < c.getId()) {
                maxId = c.getId();
            }
        }
        card.setId(maxId + 1);
        deck.addCard(card);
        deckRepository.save(deck);
        return deck;
    }

    @Override
    public Deck editCard(String id, Card card) {
        Deck deck = findDeckById(id);
        deck.getCards().forEach(e -> {
            if (e.getId().equals(card.getId())) {
                updateCard(e, card);
            }
        });
        deckRepository.save(deck);
        return deck;
    }

    @Override
    public Deck removeCard(String id, Long cardId) {
        Deck deck = findDeckById(id);
        boolean deleted = deck.getCards().removeIf(e -> e.getId().equals(cardId));
        if (!deleted) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Card doesn't exist");
        }
        deckRepository.save(deck);
        return deck;
    }

    @Override
    public void deleteAll() {
        deckRepository.deleteAll();
    }

    private void updateCard(Card cardOld, Card cardNew) {
        cardNew.setFront(cardNew.getFront());
        cardOld.setBack(cardNew.getBack());
    }

    private Deck findDeckById(String id) {
        return deckRepository.findById(id).orElseThrow(() -> {throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deck not found");});
    }

    private String createPermaLinkFromName(String name) {
        return name.toLowerCase().replaceAll("[^a-z0-9 ]", "").replaceAll("[ ]", "-");
    }
}
