package pjwstk.s16735.socialflashcardsapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;

public class ApplicationUser {
    @Id
    @JsonIgnore
    private String id;
    private String username;
    private String password;

    public ApplicationUser() {
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
