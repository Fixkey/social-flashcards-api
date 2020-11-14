package pjwstk.s16735.socialflashcardsapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pjwstk.s16735.socialflashcardsapi.model.Card;
import pjwstk.s16735.socialflashcardsapi.model.Deck;
import pjwstk.s16735.socialflashcardsapi.repository.DeckRepository;
import pjwstk.s16735.socialflashcardsapi.service.DeckService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/decks")
public class DeckController {
    private DeckService deckService;

    public DeckController(@Autowired DeckService deckService) {
        this.deckService = deckService;
    }

    @GetMapping("")
    public List<Deck> getAllDecks(Authentication authentication) {
        return deckService.getAllDecks(extractUser(authentication));
    }

    @GetMapping("/{id}")
    public Deck getDeckById(@PathVariable("id") final String id, Authentication authentication) {
        return deckService.getDeckById(id, extractUser(authentication));
    }

    @GetMapping("/perma-link/{permaLink}")
    public Deck getDeckByPermaLink(@PathVariable("permaLink") final String permaLink, @RequestParam(name = "secret", required = false) String secret, Authentication authentication) {
        return deckService.getDeckByPermaLink(permaLink, secret, extractUser(authentication));
    }

    @DeleteMapping("/{id}")
    public void removeDeckById(@PathVariable("id") final String id, Authentication authentication) {
        deckService.removeDeckById(id, extractUser(authentication));
    }

    @PostMapping("")
    public Deck createDeck(@Valid @RequestBody Deck deck, Authentication authentication, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Validation error");
        }
        return deckService.createDeck(deck, extractUser(authentication));
    }

    @PutMapping("")
    public Deck editDeck(@RequestBody Deck deck, Authentication authentication) {
        return deckService.editDeck(deck, extractUser(authentication));
    }

    @PostMapping("/{id}/card")
    public Deck addCard(@PathVariable("id") final String id, @RequestBody Card card, Authentication authentication) {
        return deckService.addCard(id, card, extractUser(authentication));
    }

    @PutMapping("/{id}/card")
    public Deck editCard(@PathVariable("id") final String id, @RequestBody Card card, Authentication authentication) {
        return deckService.editCard(id, card, extractUser(authentication));
    }

    @DeleteMapping("/{id}/card/{cardId}")
    public Deck deleteCard(@PathVariable("id") final String id, @PathVariable("cardId") final Long cardId, Authentication authentication) {
        return deckService.removeCard(id, cardId, extractUser(authentication));
    }

    private String extractUser(Authentication authentication) {
        return authentication == null ? null : (String) authentication.getPrincipal();
    }
}
