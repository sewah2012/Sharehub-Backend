package com.luslusdawmpfe.PFEBackent.services;
import com.luslusdawmpfe.PFEBackent.dtos.*;
import com.luslusdawmpfe.PFEBackent.entities.AppUser;
import com.luslusdawmpfe.PFEBackent.exceptions.EntityAlreadyExistException;
import com.luslusdawmpfe.PFEBackent.exceptions.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface UserService {
    String signup(SignupDto user, String siteUrl) throws EntityNotFoundException, MessagingException, UnsupportedEncodingException, EntityAlreadyExistException;
    String verifyEmail(String verificationCode) throws EntityNotFoundException;
    ResponseEntity<AppUserDto> loggedInUserDetails(@AuthenticationPrincipal AppUser user) throws EntityNotFoundException;

    String createUser(CreateUserDto user) throws EntityAlreadyExistException, MessagingException, UnsupportedEncodingException;

    String forgetPassword(String email) throws EntityNotFoundException, MessagingException, UnsupportedEncodingException;
    String verifyPasswordReset(String verificationCode) throws EntityNotFoundException, MessagingException, UnsupportedEncodingException;

    String deleteUser(String username) throws EntityNotFoundException;

    String resetPassword(String username, AppUser user) throws EntityNotFoundException, MessagingException, UnsupportedEncodingException;



    List<AppUserDto> listAllUsers();
    AppUserDto getSingleUser(String username) throws EntityNotFoundException;

    AppUserDto completeRegistration(AppUser user, ResgistrationCompletionDto registrationCompletionDto);

    String updateUser(AppUser user, UpdateUserRequest registrationCompletionDto) throws EntityNotFoundException;
}
