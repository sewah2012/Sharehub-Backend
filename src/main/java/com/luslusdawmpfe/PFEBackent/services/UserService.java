package com.luslusdawmpfe.PFEBackent.services;
import com.luslusdawmpfe.PFEBackent.dtos.AppUserDto;
import com.luslusdawmpfe.PFEBackent.dtos.CreateUserDto;
import org.springframework.http.ResponseEntity;

public interface UserService {
    String addNewUser(CreateUserDto user, String siteUrl) throws Exception;
    ResponseEntity<AppUserDto> getUser(Long userId) throws Exception;



}
