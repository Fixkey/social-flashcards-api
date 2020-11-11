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
import pjwstk.s16735.socialflashcardsapi.repository.ApplicationUserRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private ApplicationUserRepository applicationUserRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(@Autowired ApplicationUserRepository applicationUserRepository,
                          @Autowired BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/sign-up")
    public ResponseEntity signUp(@Valid @RequestBody ApplicationUser user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Validation error");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
//        var z = applicationUserRepository.findByUsernameMatches("(?i)^" + user.getUsername() + "$");
//      if (applicationUserRepository.findByUsername(user.getUsername()) != null) {
        if (applicationUserRepository.findByUsernameMatches("(?i)^" + user.getUsername() + "$") != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username: \"" + user.getUsername() + "\" is taken");
        }
        applicationUserRepository.save(user);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/")
    public List<String> getUsers(@RequestParam(name = "search", required = false) String search, Authentication authentication) {
//        if (authentication == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        List<ApplicationUser> list = applicationUserRepository.findAllByUsernameMatches(search != null ? ("(?i)" + search) : ".");
        return list.stream().map(e -> e.getUsername()).limit(50).collect(Collectors.toList());
    }
}
