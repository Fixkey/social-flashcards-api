package pjwstk.s16735.socialflashcardsapi.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pjwstk.s16735.socialflashcardsapi.model.ApplicationUser;
import pjwstk.s16735.socialflashcardsapi.model.Card;
import pjwstk.s16735.socialflashcardsapi.model.Deck;
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

    @PostMapping("/populate-db")
    public void populateDb() {
        deckService.deleteAll();
        Deck deck1 = new Deck();
        deck1.setName("Deck 1");
//        List<Card> list = new ArrayList<>();
        for (long i = 1; i<50; i++) {
            Card card = new Card();
            card.setFront(RandomStringUtils.randomAlphabetic(5, 15));
            card.setBack(RandomStringUtils.randomAlphabetic(5, 15));
            deck1.addCard(card);
        }
//        deck1.setCards(list);
        deckService.createDeck(deck1);

        applicationUserRepository.deleteAll();
        ApplicationUser user = new ApplicationUser();
        user.setUsername("admin");
        user.setPassword(bCryptPasswordEncoder.encode("admin123"));
        applicationUserRepository.save(user);
    }
}
