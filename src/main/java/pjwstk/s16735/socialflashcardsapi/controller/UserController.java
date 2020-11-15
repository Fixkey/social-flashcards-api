package pjwstk.s16735.socialflashcardsapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pjwstk.s16735.socialflashcardsapi.model.ApplicationUser;
import pjwstk.s16735.socialflashcardsapi.model.json.ProgressBody;
import pjwstk.s16735.socialflashcardsapi.repository.ApplicationUserRepository;
import pjwstk.s16735.socialflashcardsapi.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity signUp(@Valid @RequestBody ApplicationUser user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Validation error");
        }
        userService.signUp(user);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/")
    public List<String> getUsers(@RequestParam(name = "search", required = false) String search, Authentication authentication) {
        if (authentication == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        return userService.getUsers(search);
    }

    @GetMapping("/progress")
    public String getProgress(Authentication authentication) {
        if (authentication == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        return userService.getProgress((String) authentication.getPrincipal());
    }

    @PutMapping("/progress")
    public void setProgress(@RequestBody ProgressBody progressBody, Authentication authentication) {
        if (authentication == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        userService.setProgress((String) authentication.getPrincipal(), progressBody.getProgress());
    }
}
