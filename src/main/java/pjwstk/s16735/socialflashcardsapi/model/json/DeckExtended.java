package pjwstk.s16735.socialflashcardsapi.model.json;

import pjwstk.s16735.socialflashcardsapi.model.Deck;

import javax.validation.constraints.NotEmpty;

public class DeckExtended extends Deck {
    private String subjectId;
    private Long cardsLength;

    public DeckExtended() {
    }

    public Deck toDeck() {
        Deck deck = new Deck();
        deck.setId(this.getId());
        deck.setName(this.getName());
        deck.setPermaLink(this.getPermaLink());
        deck.setCards(this.getCards());
        deck.setPrivateDeck(this.getPrivateDeck());
        deck.setOwners(this.getOwners());
        deck.setSecret(this.getSecret());
        deck.setSubject(this.getSubject());
        return deck;
    }

    public Long getCardsLength() {
        return cardsLength;
    }

    public void setCardsLength(Long cardsLength) {
        this.cardsLength = cardsLength;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
}
