package com.luslusdawmpfe.PFEBackent.controllers;
import com.luslusdawmpfe.PFEBackent.configs.TokenProvider;
import com.luslusdawmpfe.PFEBackent.dtos.*;
import com.luslusdawmpfe.PFEBackent.entities.AppUser;
import com.luslusdawmpfe.PFEBackent.exceptions.EntityNotFoundException;
import com.luslusdawmpfe.PFEBackent.services.UserService;
import com.luslusdawmpfe.PFEBackent.utils.AppAdminOrOwner;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
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
        return ResponseEntity.ok(userService.signup(user,getSiteUrl(req)));
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

    @GetMapping("/forgetPassword")
    ResponseEntity<String> forgotPassword(@RequestParam("email") String email) throws MessagingException, UnsupportedEncodingException, EntityNotFoundException {
        return ResponseEntity.ok(userService.forgetPassword(email));
    }

    @GetMapping("/verifyResetPasswordCode")
    ResponseEntity<String> verifyResetPasswordCode(@RequestParam("code") String verificationCode) throws Exception {
        return ResponseEntity.ok(userService.verifyPasswordReset(verificationCode));
    }

    @PreAuthorize("hasAnyAuthority('APP_ADMIN')")
    @PostMapping("/createUser")
    ResponseEntity<String> createUSer(@RequestBody CreateUserDto user) throws Exception {
        return ResponseEntity.ok(userService.createUser(user));
    }
    //TODO: delete user
    @AppAdminOrOwner
    @GetMapping("/deleteUser/{username}")
    ResponseEntity<String> deleteUser(@PathVariable String username) throws EntityNotFoundException {
        return ResponseEntity.ok(userService.deleteUser(username));
    }

    //TODO: ResetPassword
    @AppAdminOrOwner
    @GetMapping("/resetPassword/{username}")
    ResponseEntity<String> resetPassword(@PathVariable String username) throws MessagingException, UnsupportedEncodingException, EntityNotFoundException {
        return ResponseEntity.ok(userService.resetPassword(username));
    }

    @PreAuthorize("hasAnyAuthority('APP_ADMIN')")
    @GetMapping("/listAppUsers")
    ResponseEntity<List<AppUserDto>> listAppUsers() {
        return ResponseEntity.ok(userService.listAllUsers());
    }

    @PreAuthorize("hasAnyAuthority('APP_ADMIN')")
    @GetMapping("/getSingleUser/{username}")
    ResponseEntity<AppUserDto> getSingleUser(@PathVariable String username) throws EntityNotFoundException {
        return ResponseEntity.ok(userService.getSingleUser(username));
    }

    @PreAuthorize("hasAnyAuthority({'APP_USER','APP_ADMIN'})")
    @GetMapping("/myDetails")
    ResponseEntity<AppUserDto> loggedInUserDetails(@AuthenticationPrincipal AppUser user) throws Exception {
        return userService.loggedInUserDetails(user);
    }

    //TODO: userInfo

    @PreAuthorize("hasAnyAuthority({'APP_USER','APP_ADMIN'})")
    @PostMapping("/completeSignup")
    ResponseEntity<AppUserDto> completeRegistration(@AuthenticationPrincipal AppUser user, @RequestBody ResgistrationCompletionDto registrationCompletionDto) throws Exception {
        return ResponseEntity.ok(userService.completeRegistration(user, registrationCompletionDto));
    }

    @PreAuthorize("hasAnyAuthority({'APP_USER','APP_ADMIN'})")
    @PutMapping("/updateUser")
    ResponseEntity<Object> updateUser(@AuthenticationPrincipal AppUser user, @RequestBody UpdateUserRequest registrationCompletionDto) throws Exception {
        return ResponseEntity.ok(userService.updateUser(user, registrationCompletionDto));
    }


    private String getSiteUrl(HttpServletRequest request){
        String siteUrl = request.getRequestURL().toString();
        return siteUrl.replace(request.getServletPath(), "");
    }
}
