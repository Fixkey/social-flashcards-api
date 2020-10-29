package pjwstk.s16735.socialflashcardsapi.model;

import org.springframework.data.annotation.Id;

public class Card {
    @Id
    private Long id;

    private String front;
    private String back;

    public Card() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }
}
