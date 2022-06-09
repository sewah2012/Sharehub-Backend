package com.luslusdawmpfe.PFEBackent.services;
import com.luslusdawmpfe.PFEBackent.dtos.AppUserDto;
import com.luslusdawmpfe.PFEBackent.dtos.CreateUserDto;
import com.luslusdawmpfe.PFEBackent.exceptions.EntityAlreadyExistException;
import com.luslusdawmpfe.PFEBackent.exceptions.EntityNotFoundException;
import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface UserService {
    String addNewUser(CreateUserDto user, String siteUrl) throws EntityNotFoundException, MessagingException, UnsupportedEncodingException, EntityAlreadyExistException;
    String verifyEmail(String verificationCode) throws EntityNotFoundException;
    ResponseEntity<AppUserDto> getUser(Long userId) throws EntityNotFoundException;



}
