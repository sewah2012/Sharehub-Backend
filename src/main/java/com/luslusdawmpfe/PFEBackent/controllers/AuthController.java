package com.luslusdawmpfe.PFEBackent.controllers;
import com.luslusdawmpfe.PFEBackent.configs.TokenProvider;
import com.luslusdawmpfe.PFEBackent.dtos.AppUserDto;
import com.luslusdawmpfe.PFEBackent.dtos.CreateUserDto;
import com.luslusdawmpfe.PFEBackent.dtos.LoginDto;
import com.luslusdawmpfe.PFEBackent.dtos.SignupDto;
import com.luslusdawmpfe.PFEBackent.entities.AppUser;
import com.luslusdawmpfe.PFEBackent.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    ResponseEntity<String> signup(@RequestBody SignupDto user, HttpServletRequest req) throws Exception {
        return ResponseEntity.ok(userService.addNewUser(user,getSiteUrl(req)));
    }

    @GetMapping("/verify")
    ResponseEntity<String> verifyEmail(@RequestParam("code") String verificationCode) throws Exception {
        return ResponseEntity.ok(userService.verifyEmail(verificationCode));
    }

    @PostMapping("/login")
    ResponseEntity<Map<String, String>> login(@RequestBody LoginDto user){
        var auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
        var token = tokenProvider.generateToken(auth);
        Map<String, String> resp = new HashMap<>();
        resp.put("message","User successfully logged in");
        resp.put("token", token);
        return ResponseEntity.status(201).body(resp);
    }

    @PreAuthorize("hasAnyAuthority('APP_ADMIN')")
    @PostMapping("/createUser")
    ResponseEntity<String> createUSer(@RequestBody CreateUserDto user) throws Exception {
        return ResponseEntity.ok(userService.createUser(user));
    }


    @PreAuthorize("hasAnyAuthority({'APP_USER','APP_ADMIN'})")
    @GetMapping("/myDetails")
    ResponseEntity<AppUserDto> loggedInUserDetails(@AuthenticationPrincipal AppUser user) throws Exception {
        return userService.loggedInUserDetails(user);
    }

    private String getSiteUrl(HttpServletRequest request){
        String siteUrl = request.getRequestURL().toString();
        return siteUrl.replace(request.getServletPath(), "");
    }
}
