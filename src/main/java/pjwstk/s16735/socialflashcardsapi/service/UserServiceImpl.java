package pjwstk.s16735.socialflashcardsapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import pjwstk.s16735.socialflashcardsapi.model.ApplicationUser;
import pjwstk.s16735.socialflashcardsapi.repository.ApplicationUserRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    private ApplicationUserRepository applicationUserRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(@Autowired ApplicationUserRepository applicationUserRepository,
                          @Autowired BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void signUp(ApplicationUser user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        if (applicationUserRepository.findByUsernameMatches("(?i)^" + user.getUsername() + "$") != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username: \"" + user.getUsername() + "\" is already taken");
        }
        applicationUserRepository.save(user);
    }

    @Override
    public List<String> getUsers(String search) {
        List<ApplicationUser> list = applicationUserRepository.findAllByUsernameMatches(search != null ? ("(?i)" + search) : ".");
        return list.stream().map(e -> e.getUsername()).limit(50).collect(Collectors.toList());
    }
}
