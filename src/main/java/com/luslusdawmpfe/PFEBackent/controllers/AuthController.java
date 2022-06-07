package com.luslusdawmpfe.PFEBackent.controllers;

import com.luslusdawmpfe.PFEBackent.configs.TokenProvider;
import com.luslusdawmpfe.PFEBackent.dtos.AppUserDto;
import com.luslusdawmpfe.PFEBackent.dtos.CreateUserDto;
import com.luslusdawmpfe.PFEBackent.dtos.LoginDto;
import com.luslusdawmpfe.PFEBackent.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    ResponseEntity<String> signup(@RequestBody CreateUserDto user){
        return ResponseEntity.ok(userService.addNewUser(user));
    }

    @PostMapping("/login")
    ResponseEntity<String> login(@RequestBody LoginDto user){
        var auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
        return ResponseEntity.status(201).header(
                HttpHeaders.AUTHORIZATION,
                tokenProvider.generateToken(auth)

        ).body("User successfully logged in. Access token provided in header");
    }

    @GetMapping("/userdetails/{userId}")
    ResponseEntity<AppUserDto> userDetailsgetUSerDetails(@PathVariable("userId") Long userId) throws Exception {
        return userService.getUser(userId);
    }
}
