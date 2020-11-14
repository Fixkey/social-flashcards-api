package pjwstk.s16735.socialflashcardsapi.service;

import pjwstk.s16735.socialflashcardsapi.model.ApplicationUser;

import java.util.List;

public interface UserService {
    void signUp(ApplicationUser user);
    List<String> getUsers(String search);
}
