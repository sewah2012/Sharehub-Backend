package com.luslusdawmpfe.PFEBackent.services.impl;

import com.luslusdawmpfe.PFEBackent.dtos.AppUserDto;
import com.luslusdawmpfe.PFEBackent.dtos.CreateUserDto;
import com.luslusdawmpfe.PFEBackent.dtos.ResgistrationCompletionDto;
import com.luslusdawmpfe.PFEBackent.dtos.SignupDto;
import com.luslusdawmpfe.PFEBackent.entities.AppUser;
import com.luslusdawmpfe.PFEBackent.entities.Role;
import com.luslusdawmpfe.PFEBackent.exceptions.EntityAlreadyExistException;
import com.luslusdawmpfe.PFEBackent.exceptions.EntityNotFoundException;
import com.luslusdawmpfe.PFEBackent.mappers.AppUserMapper;
import com.luslusdawmpfe.PFEBackent.mappers.RoleMapper;
import com.luslusdawmpfe.PFEBackent.repos.AppUserRepo;
import com.luslusdawmpfe.PFEBackent.repos.RoleRepo;
import com.luslusdawmpfe.PFEBackent.services.UserService;
import com.luslusdawmpfe.PFEBackent.utils.EmailSender;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
//@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private AppUserRepo appUserRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AppUserMapper mapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private RoleRepo roleRepo;

    final String DEFAULT_APP_USER_ROLE_PASSWORD = "sharehub123";

    @Override
    @Transactional
    public String addNewUser(SignupDto createUserDto, String siteUrl) throws MessagingException, UnsupportedEncodingException, EntityAlreadyExistException {
        log.info("saving user to db");
        log.info("User email: "+ createUserDto.getEmail());
        if(appUserRepo.findUserByEmail(createUserDto.getEmail()).isPresent()) throw new EntityAlreadyExistException("USer already exist in system");
        var role = roleRepo.findRoleByName("APP_USER").orElse(new Role(null,"APP_USER","App User Role"));
        AppUser user = appUserRepo.save(
                        AppUser.builder()
                                .username(createUserDto.getUsername())
                                .password(passwordEncoder.encode(createUserDto.getPassword()))
                                .firstName(createUserDto.getFirstName())
                                .lastName(createUserDto.getLastName())
                                .email(createUserDto.getEmail())
                                .verificationCode(RandomString.make(5)) // revert to this for production
//                                .verificationCode("12345") //for testing purposes
                                .isEnabled(true)
                                .roles(List.of(role))
                                .build()
                );
        log.info("initiating email sending");
        emailSender.sendVerificationEmail(user, siteUrl);
        return "A activation email has been sent to your email address.";
    }

    @Override
    public String createUser(CreateUserDto createUserDto) throws EntityAlreadyExistException, MessagingException, UnsupportedEncodingException {
        log.info("saving user to db");
        log.info("User email: "+ createUserDto.getEmail());
        if(appUserRepo.findUserByEmail(createUserDto.getEmail()).isPresent()) throw new EntityAlreadyExistException("USer already exist in system");
        var roless = createUserDto.getRoles().stream()
                .map(roleMapper::roleDtoToRole)
                .collect(Collectors.toList());

        var role = roless.stream()
                .map(
                (r)-> roleRepo.findRoleByName(r.getName()).orElse(new Role(null,r.getName(),r.getDescription()))
        ).collect(Collectors.toList());

        var randomPassword = RandomString.make(8);

        AppUser user = appUserRepo.save(
                AppUser.builder()
                        .username(createUserDto.getUsername())
                        .password(passwordEncoder.encode(DEFAULT_APP_USER_ROLE_PASSWORD)) // just for testing ... will change to random string
                        .firstName(createUserDto.getFirstName())
                        .lastName(createUserDto.getLastName())
                        .nickname(createUserDto.getNickname())
                        .dateOfBirth(createUserDto.getDateOfBirth())
                        .address(createUserDto.getAddress())
                        .email(createUserDto.getEmail())
                        .website(createUserDto.getWebsite())
                        .imageUrl(createUserDto.getImageUrl())
                        .isEnabled(true)
                        .roles(role)
                        .build()
        );

        emailSender.sendUserCredentials(user, DEFAULT_APP_USER_ROLE_PASSWORD);
        return "Admin Create user successfully. login details are send to user email.";
    }

    @Override
    public String verifyEmail(String verificationCode) throws EntityNotFoundException {
     var x = appUserRepo.findUserByVerificationCode(verificationCode).map(
                (u)->{
                    log.info("Found user: "+u.getVerificationCode());
                    u.setVerificationCode(null);
                    u.setIsEnabled(true);
                   return appUserRepo.save(u);
                }
        ).orElseThrow(()->new EntityNotFoundException("Verification failed"));

        return "Verification completed successfully";
    }

    @Override
    public String verifyPasswordReset(String verificationCode) throws EntityNotFoundException, MessagingException, UnsupportedEncodingException {
        var randomPassword = RandomString.make(8);
        var user = appUserRepo.findUserByVerificationCode(verificationCode).map(
                (u)->{
                    log.info("Found user: "+u.getVerificationCode());
                    u.setVerificationCode(null);
                    u.setIsEnabled(true);
                    u.setPassword(passwordEncoder.encode(randomPassword));
                    return appUserRepo.save(u);
                }
        ).orElseThrow(()->new EntityNotFoundException("Verification failed"));

        emailSender.sendResetPasswordCredentials(user, randomPassword);

        return "Verification completed successfully";
    }

    @Override
    public ResponseEntity<AppUserDto> loggedInUserDetails(@AuthenticationPrincipal AppUser user) throws EntityNotFoundException {

        var usr = appUserRepo.findById(user.getId())
                .map((x)->mapper.mapToAppUserDto(x))
                .orElseThrow(()->new EntityNotFoundException("User not found!"));
        return ResponseEntity.ok(usr);
    }

    @Override
    public String forgetPassword(String email) throws EntityNotFoundException, MessagingException, UnsupportedEncodingException {
        var user = appUserRepo.findUserByEmail(email).orElseThrow( ()->new EntityNotFoundException("User not found!"));
        user.setVerificationCode("12345"); // change later to RandomString
        user.setIsEnabled(false);
        appUserRepo.save(user);
        emailSender.sendResetPasswordLink(user);
        return "An Email has been sent with details to reset your password..";
    }

    @Override
    public String deleteUser(String username) throws EntityNotFoundException {
        var user = Optional.of(appUserRepo.findUserByUsername(username)).orElseThrow( ()->new EntityNotFoundException("User not found!"));
        appUserRepo.delete(user);
        return "User successfully deleted";
    }

    @Override
    public String resetPassword(String username) throws EntityNotFoundException, MessagingException, UnsupportedEncodingException {
        var randomPassword = RandomString.make(8);
        var user = Optional.of(appUserRepo.findUserByUsername(username))
                .map(
                        (u)->{
                            u.setIsEnabled(true);
                            u.setPassword(passwordEncoder.encode(randomPassword));
                            return appUserRepo.save(u);
                        }
                )
                .orElseThrow( ()->new EntityNotFoundException("User not found!"));

        emailSender.sendResetPasswordCredentials(user, randomPassword);

        return "Password successfully reset. An email with has been sent to your email.";
    }

    @Override
    public List<AppUserDto> listAllUsers() {
        return appUserRepo.findAll().stream().map(mapper::mapToAppUserDto).collect(Collectors.toList());
    }

    @Override
    public AppUserDto getSingleUser(String username) throws EntityNotFoundException {
       return Optional.of(appUserRepo.findUserByUsername(username))
                .map((x)->mapper.mapToAppUserDto(x))
                .orElseThrow(()->new EntityNotFoundException("User not found!"));
    }


    @Override
    public String completeRegistration(AppUser user, ResgistrationCompletionDto registrationCompletionDto) {
        user.setWebsite(registrationCompletionDto.getWebsite());
        user.setImageUrl(registrationCompletionDto.getImageUrl());
        user.setNickname(registrationCompletionDto.getNickname());
        user.setDateOfBirth(registrationCompletionDto.getDateOfBirth());
        user.setAddress(registrationCompletionDto.getAddress());
        user.setRegistrationCompleted(true);

        var completedUser = appUserRepo.save(user);

        log.info("Is user registration complete: "+completedUser.isRegistrationCompleted());

        return completedUser.isRegistrationCompleted() ? "User Signup successfully completed" : "Error completing signup";
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
           return Optional.of(appUserRepo.findUserByUsername(username))
                    .orElseThrow(()->new UsernameNotFoundException("User not found!"));

    }




}
