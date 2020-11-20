package pjwstk.s16735.socialflashcardsapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pjwstk.s16735.socialflashcardsapi.model.Deck;
import pjwstk.s16735.socialflashcardsapi.model.Subject;
import pjwstk.s16735.socialflashcardsapi.repository.SubjectRepository;
import pjwstk.s16735.socialflashcardsapi.service.SubjectService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/subject")
public class SubjectController {
    private SubjectService subjectService;

    public SubjectController(@Autowired SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping("/all-categories")
    public List<String> getAllCategories() {
        return subjectService.getAllCategories();
    }

    @GetMapping("/category/{category}")
    public List<Subject> getAllSubjects(@PathVariable("category") final String category) {
        return subjectService.getAllSubjects(category);
    }

    @PostMapping("")
    public Subject createSubject(@Valid @RequestBody Subject subject, Authentication authentication, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Validation error");
        }
        if (!authentication.getPrincipal().toString().equals("administrator")) { // admin list
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return subjectService.createSubject(subject);
    }
}
