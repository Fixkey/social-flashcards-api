package pjwstk.s16735.socialflashcardsapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ApplicationUser {
    @Id
    @JsonIgnore
    private String id;
    @NotEmpty(message = "Username can't be empty")
    @Pattern(regexp = "[a-zA-Z0-9]+", message = "No special characters")
    @Size(min = 6, message = "Username must be at least 6 chars long")
    private String username;
    @NotEmpty(message = "Password can't be empty")
    @Size(min = 6, message = "Password must be at least 6 chars long")
    private String password;

    @JsonIgnore
    private String progress;

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

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }
}
