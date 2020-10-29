package pjwstk.s16735.socialflashcardsapi.service;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pjwstk.s16735.socialflashcardsapi.model.Deck;
import pjwstk.s16735.socialflashcardsapi.repository.DeckRepository;

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
        DeckService deckService = new DeckServiceImpl(deckRepository);
        Deck deck = new Deck();
        deck.setName("ABC DBC1 3");
        deckService.createDeck(deck);

        verify(deckRepository, atLeastOnce());
        Assert.assertEquals(deck.getPermaLink(), "abc-dbc1-3");
    }
}
