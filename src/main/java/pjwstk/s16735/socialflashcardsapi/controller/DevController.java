package pjwstk.s16735.socialflashcardsapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pjwstk.s16735.socialflashcardsapi.model.Card;
import pjwstk.s16735.socialflashcardsapi.model.Deck;
import pjwstk.s16735.socialflashcardsapi.repository.DeckRepository;
import pjwstk.s16735.socialflashcardsapi.service.DeckService;

import java.util.Arrays;

@RestController
@RequestMapping("dev")
public class DevController {
    private DeckService deckService;

    public DevController(@Autowired DeckService deckService) {
        this.deckService = deckService;
    }

    @PostMapping("/populate-db")
    public void populateDb() {
        deckService.deleteAll();
        Deck deck1 = new Deck();
        deck1.setName("Deck 1");
        Card card1 = new Card();
        Card card2 = new Card();
        card1.setFront("Hello");
        card1.setBack("Czesc");
        card1.setId(1L);
        card2.setFront("Bye");
        card2.setBack("Pa");
        card2.setId(2L);
        deck1.setCards(Arrays.asList(card1, card2));
        deckService.createDeck(deck1);
    }
}
