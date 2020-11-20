package pjwstk.s16735.socialflashcardsapi.service;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pjwstk.s16735.socialflashcardsapi.model.Deck;
import pjwstk.s16735.socialflashcardsapi.model.json.DeckExtended;
import pjwstk.s16735.socialflashcardsapi.repository.DeckRepository;

import java.io.IOException;
import java.net.URL;

import static org.mockito.Mockito.*;

public class DeckServiceImplTest {
//    private DeckService deckService;

//    @Mock
//    private DeckRepository deckRepository;
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        deckService = new DeckServiceImpl(deckRepository);
//    }

//    @Test
//    public void getById_ValidId() {
//
//        doReturn(someMockData).when(myRepository).findOne("1");
//        MyObject myObject = myService.getById("1");
//
//        //Whatever asserts need to be done on the object myObject
//    }
    @Test
    void createDeck() {
        DeckRepository deckRepository = mock(DeckRepository.class);
        DeckService deckService = new DeckServiceImpl(deckRepository, null);
        DeckExtended deck = new DeckExtended();
        deck.setName("ABC DBC1 3");
        deckService.createDeck(deck, "administrator");

        loadTestJson("./BasicJapaneseDeck.json");

        verify(deckRepository, atLeastOnce());
        Assert.assertEquals(deck.getPermaLink(), "abc-dbc1-3");
    }

    public String loadTestJson(String fileName) {
        URL url = Resources.getResource(DeckServiceImplTest.class, fileName);
        try {
            String data = Resources.toString(url, Charsets.UTF_8);
            return data;
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load a JSON file.", e);
        }
    }
}
