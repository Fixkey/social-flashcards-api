package pjwstk.s16735.socialflashcardsapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Deck {
    @Id
    private String id;

    @NotEmpty(message = "Deck's name can't be empty")
    @Size(min = 3, message = "Deck's name must be at least 3 characters long")
    private String name;
    private String permaLink;
    private List<Card> cards = new ArrayList<>();
    private Boolean privateDeck = false;
    private Set<String> owners = new HashSet<>();
    private String secret = RandomStringUtils.randomAlphanumeric(8);

    @JsonIgnore
    private Long counter = 0L;

    public Deck() {
    }

    public void addOwner(String ownerId) {
        owners.add(ownerId);
    }

    public Boolean getPrivateDeck() {
        return privateDeck;
    }

    public void setPrivateDeck(Boolean privateDeck) {
        this.privateDeck = privateDeck;
    }

    public Set<String> getOwners() {
        return owners;
    }

    public void setOwners(Set<String> owners) {
        this.owners = owners;
    }

    public void addCard(Card card) {
        card.setId(counter++);
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

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
