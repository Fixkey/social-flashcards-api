package pjwstk.s16735.socialflashcardsapi.model;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    @Id
    private String id;

    private String name;
    private String permaLink;
    private List<Card> cards = new ArrayList<>();

    public Deck() {
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void removeCard(Card card) {
        cards.remove(card);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermaLink() {
        return permaLink;
    }

    public void setPermaLink(String permaLink) {
        this.permaLink = permaLink;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
