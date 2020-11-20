package pjwstk.s16735.socialflashcardsapi.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pjwstk.s16735.socialflashcardsapi.model.ApplicationUser;
import pjwstk.s16735.socialflashcardsapi.model.Card;
import pjwstk.s16735.socialflashcardsapi.model.Deck;
import pjwstk.s16735.socialflashcardsapi.model.json.DeckExtended;
import pjwstk.s16735.socialflashcardsapi.repository.ApplicationUserRepository;
import pjwstk.s16735.socialflashcardsapi.repository.DeckRepository;
import pjwstk.s16735.socialflashcardsapi.service.DeckService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("dev")
public class DevController {
    private DeckService deckService;
    private ApplicationUserRepository applicationUserRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public DevController(@Autowired DeckService deckService, @Autowired ApplicationUserRepository applicationUserRepository,
                         @Autowired BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.deckService = deckService;
        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    // auth workaround
    @GetMapping("/populate-db")
    public void populateDb() {
        deckService.deleteAll();
        DeckExtended deck1 = new DeckExtended();
        deck1.setName("Deck 1");
        deck1.setPrivateDeck(true);
        deck1.addOwner("administrator");
        populateCards(deck1, "administrator");

        applicationUserRepository.deleteAll();
        ApplicationUser user = new ApplicationUser();
        user.setUsername("administrator");
        user.setPassword(bCryptPasswordEncoder.encode("admin123"));
        applicationUserRepository.save(user);

        populateUsers(50);

        DeckExtended deck2 = new DeckExtended();
        deck2.setName("Deck 2");
        populateCards(deck2, "administrator2");
    }

    private void populateCards(DeckExtended deck, String user) {
        for (long i = 1; i<50; i++) {
            Card card = new Card();
            card.setFront(RandomStringUtils.randomAlphabetic(5, 15) + i);
            card.setBack(RandomStringUtils.randomAlphabetic(5, 15) + i);
            deck.addCard(card);
        }
        deckService.createDeck(deck, user);
    }

    private void populateUsers(int count) {
        for (int i = 1; i<count; i++) {
            ApplicationUser user = new ApplicationUser();
            user.setUsername(RandomStringUtils.randomAlphabetic(10, 20) + i);
            user.setPassword(bCryptPasswordEncoder.encode("admin123"));
            applicationUserRepository.save(user);
        }
    }
}
