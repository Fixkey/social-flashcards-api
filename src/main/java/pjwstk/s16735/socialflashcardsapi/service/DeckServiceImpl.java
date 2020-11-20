package pjwstk.s16735.socialflashcardsapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pjwstk.s16735.socialflashcardsapi.model.Card;
import pjwstk.s16735.socialflashcardsapi.model.Deck;
import pjwstk.s16735.socialflashcardsapi.model.Subject;
import pjwstk.s16735.socialflashcardsapi.model.json.DeckExtended;
import pjwstk.s16735.socialflashcardsapi.repository.DeckRepository;
import pjwstk.s16735.socialflashcardsapi.repository.SubjectRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeckServiceImpl implements DeckService {
    private DeckRepository deckRepository;
    private SubjectService subjectService;

    public DeckServiceImpl(@Autowired DeckRepository deckRepository, @Autowired SubjectService subjectService) {
        this.deckRepository = deckRepository;
        this.subjectService = subjectService;
    }

    @Override
    public List<Deck> getAllDecks(String user) {

        return filterDecksByUser(deckRepository.findAll(), user);
    }

    // candidate for caching
    @Override
    public List<DeckExtended> getAllDecksBySubjectShallow(String subjectId, String user) {
        List<Deck> decks = filterDecksByUser(deckRepository.findDecksBySubjectIdQuery(subjectId), user);
        return decks.stream().map(deck -> {
            DeckExtended shallowDeck = new DeckExtended();
            shallowDeck.setSecret(null);
            shallowDeck.setPrivateDeck(deck.getPrivateDeck());
            shallowDeck.setId(deck.getId());
            shallowDeck.setPermaLink(deck.getPermaLink());
            shallowDeck.setName(deck.getName());
            shallowDeck.setCardsLength((long) deck.getCards().size());
            return shallowDeck;
        }).collect(Collectors.toList());
    }

    private List<Deck> filterDecksByUser(List<Deck> deck, String user) {
        return deck.stream().filter(deck1 -> {
            if (deck1.getPrivateDeck()) {
                return deck1.getOwners().contains(user);
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
    public Deck createDeck(DeckExtended deckExtended, String user) {
        deckExtended.setId(null);
        deckExtended.setPermaLink(createPermaLinkFromName(deckExtended.getName()));
        if (deckRepository.findByNameMatches("(?i)^" + deckExtended.getName() + "$") != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deck name " + deckExtended.getName() + " is already taken");
        }
        if (deckRepository.findDeckByPermaLinkEquals("(?i)^" + deckExtended.getPermaLink() + "$") != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deck permalink " + deckExtended.getPermaLink() + " is already taken, try a different name");
        }

        if (deckExtended.getSubjectId() != null) {
            Subject subject = subjectService.getSubjectById(deckExtended.getSubjectId());
            if (subject == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Subject not found");
            }
            deckExtended.setSubject(subject);
        }

        deckExtended.addOwner(user);
        Deck savedDeck = deckRepository.insert(deckExtended.toDeck());
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
    public Deck changeOwnership(Deck deck, String user) {
        Deck foundDeck = findDeckById(deck.getId());
        throwIfNotAuthorizedOrNull(user, foundDeck);
        foundDeck.setPrivateDeck(deck.getPrivateDeck());
        foundDeck.setOwners(deck.getOwners());
        foundDeck.addOwner(user);
        return deckRepository.save(foundDeck);
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
