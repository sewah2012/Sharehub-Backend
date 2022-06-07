package com.luslusdawmpfe.PFEBackent.services;
import com.luslusdawmpfe.PFEBackent.dtos.AppUserDto;
import com.luslusdawmpfe.PFEBackent.dtos.CreateUserDto;
import com.luslusdawmpfe.PFEBackent.entities.AppUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    String addNewUser(CreateUserDto user);
    ResponseEntity<AppUserDto> getUser(Long userId) throws Exception;

}
