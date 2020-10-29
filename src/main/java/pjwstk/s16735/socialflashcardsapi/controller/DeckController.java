package pjwstk.s16735.socialflashcardsapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pjwstk.s16735.socialflashcardsapi.model.Card;
import pjwstk.s16735.socialflashcardsapi.model.Deck;
import pjwstk.s16735.socialflashcardsapi.repository.DeckRepository;
import pjwstk.s16735.socialflashcardsapi.service.DeckService;

import java.util.List;

@RestController
@RequestMapping("/deck")
public class DeckController {
    private DeckService deckService;

    public DeckController(@Autowired DeckService deckService) {
        this.deckService = deckService;
    }

    @GetMapping("")
    public List<Deck> getAllDecks() {
        return deckService.getAllDecks();
    }

    

    @GetMapping("/{id}")
    public Deck getDeckById(@PathVariable("id") final String id) {
        return deckService.getDeckById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity removeDeckById(@PathVariable("id") final String id) {
        deckService.removeDeckById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("")
    public Deck createDeck(@RequestBody Deck deck) {
        deck.setId(null);
        return deckService.createDeck(deck);
    }

    @PutMapping("")
    public Deck editDeck(@RequestBody Deck deck) {
        return deckService.editDeck(deck);
    }

    @PostMapping("/{id}/card")
    public Deck addCard(@PathVariable("id") final String id, @RequestBody Card card) {
        return deckService.addCard(id, card);
    }

    @PutMapping("/{id}/card")
    public Deck editCard(@PathVariable("id") final String id, @RequestBody Card card) {
        return deckService.editCard(id, card);
    }

    @DeleteMapping("/{id}/card/{cardId}")
    public Deck deleteCard(@PathVariable("id") final String id, @PathVariable("cardId") final Long cardId) {
        return deckService.removeCard(id, cardId);
    }
}
