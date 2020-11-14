package pjwstk.s16735.socialflashcardsapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pjwstk.s16735.socialflashcardsapi.model.Card;
import pjwstk.s16735.socialflashcardsapi.model.Deck;
import pjwstk.s16735.socialflashcardsapi.repository.DeckRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeckServiceImpl implements DeckService {
    private DeckRepository deckRepository;

    public DeckServiceImpl(@Autowired DeckRepository deckRepository) {
        this.deckRepository = deckRepository;
    }

    @Override
    public List<Deck> getAllDecks(String user) {
        return deckRepository.findAll().stream().filter(deck -> {
            if (deck.getPrivateDeck()) {
                return deck.getOwners().contains(user);
            } else {
                return true;
            }
        }).collect(Collectors.toList());
    }

    @Override
    public Deck getDeckById(String id, String user) {
        Deck deck = findDeckById(id);
        throwIfNotAuthorizedOrNull(user, deck, false);
        return deck;
    }

    @Override
    public Deck getDeckByPermaLink(String permaLink, String secret, String user) {
        Deck deck = deckRepository.findDeckByPermaLinkEquals(permaLink);
        if (deck.getSecret().equals(secret)) return deck;
        throwIfNotAuthorizedOrNull(user, deck, false);
        return deck;
    }

    @Override
    public Deck createDeck(Deck deck, String user) {
        deck.setId(null);
        deck.setPermaLink(createPermaLinkFromName(deck.getName()));
        if (deckRepository.findByNameMatches("(?i)^" + deck.getName() + "$") != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deck name " + deck.getName() + " is already taken");
        }
        if (deckRepository.findDeckByPermaLinkEquals("(?i)^" + deck.getPermaLink() + "$") != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deck permalink " + deck.getPermaLink() + " is already taken, try a different name");
        }
        deck.addOwner(user);
        Deck savedDeck = deckRepository.insert(deck);
        return savedDeck;
    }

    @Override
    public Deck editDeck(Deck deck, String user) {
        Deck foundDeck = findDeckById(deck.getId());
        throwIfNotAuthorizedOrNull(user, foundDeck);
        deckRepository.save(deck);
        return deck;
    }

    @Override
    public void removeDeckById(String id, String user) {
        Deck foundDeck = findDeckById(id);
        throwIfNotAuthorizedOrNull(user, foundDeck);
        deckRepository.deleteById(id);
    }

    @Override
    public Deck addCard(String id, Card card, String user) {
        Deck deck = findDeckById(id);
        throwIfNotAuthorizedOrNull(user, deck);
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
    public Deck editCard(String id, Card card, String user) {
        if (card.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Card must have id");
        }
        Deck deck = findDeckById(id);
        throwIfNotAuthorizedOrNull(user, deck);
        deck.getCards().forEach(e -> {
            if (e.getId().equals(card.getId())) {
                updateCard(e, card);
            }
        });
        deckRepository.save(deck);
        return deck;
    }

    @Override
    public Deck removeCard(String id, Long cardId, String user) {
        Deck deck = findDeckById(id);
        throwIfNotAuthorizedOrNull(user, deck);
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
        cardOld.setFront(cardNew.getFront());
        cardOld.setBack(cardNew.getBack());
    }

    private Deck findDeckById(String id) {
        return deckRepository.findById(id).orElseThrow(() -> {throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deck not found");});
    }

    private String createPermaLinkFromName(String name) {
        return name.toLowerCase().replaceAll("[^a-z0-9 ]", "").replaceAll("[ ]", "-");
    }

    private boolean throwIfNotAuthorized(String user, Deck deck) {
        if (deck.getOwners().contains(user)) {
            return true;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized");
        }
    }

    private void throwIfNotAuthorizedOrNull(String user, Deck deck) {
        throwIfNotAuthorizedOrNull(user, deck, true);
    }


    private void throwIfNotAuthorizedOrNull(String user, Deck deck, boolean edit) {
        if (deck == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Deck not found");
        }
        if (edit || deck.getPrivateDeck()) {
            throwIfNotAuthorized(user, deck);
        }
    }
}
