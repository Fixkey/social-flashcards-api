package pjwstk.s16735.socialflashcardsapi.model;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

public class Subject {
    @Id
    private String id;
    @NotEmpty(message = "Category can't be empty")
    private String category;
    @NotEmpty(message = "Name can't be empty")
    private String name;

    public Subject() {
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return getCategory().equals(subject.getCategory()) &&
                getName().equals(subject.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCategory(), getName());
    }
}
