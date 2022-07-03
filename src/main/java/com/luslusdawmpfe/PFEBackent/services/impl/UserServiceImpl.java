package com.luslusdawmpfe.PFEBackent.services.impl;

import com.luslusdawmpfe.PFEBackent.dtos.*;
import com.luslusdawmpfe.PFEBackent.entities.AppUser;
import com.luslusdawmpfe.PFEBackent.entities.Role;
import com.luslusdawmpfe.PFEBackent.exceptions.EntityAlreadyExistException;
import com.luslusdawmpfe.PFEBackent.exceptions.EntityNotFoundException;
import com.luslusdawmpfe.PFEBackent.mappers.AppUserMapper;
import com.luslusdawmpfe.PFEBackent.mappers.RoleMapper;
import com.luslusdawmpfe.PFEBackent.repos.AppUserRepo;
import com.luslusdawmpfe.PFEBackent.repos.AttachementRepo;
import com.luslusdawmpfe.PFEBackent.repos.RoleRepo;
import com.luslusdawmpfe.PFEBackent.services.UserService;
import com.luslusdawmpfe.PFEBackent.utils.EmailSender;
import com.luslusdawmpfe.PFEBackent.utils.SecurityCheck;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
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

    @Autowired
    private AttachementRepo attachementRepo;

    final String DEFAULT_APP_USER_ROLE_PASSWORD = "sharehub123";

    @Override
    @Transactional
    public String signup(SignupDto createUserDto, String siteUrl) throws MessagingException, UnsupportedEncodingException, EntityAlreadyExistException {
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
                                .verificationCode(generateCode()) // revert to this for production
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
                        .password(passwordEncoder.encode(randomPassword)) // just for testing ... will change to random string
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

        emailSender.sendUserCredentials(user, randomPassword);
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
                    u.setResetPassword(true);
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
        user.setVerificationCode(generateCode()); // change later to RandomString
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
    public String resetPassword(String username, AppUser user) throws EntityNotFoundException, MessagingException, UnsupportedEncodingException {
        var randomPassword = RandomString.make(8);
        var au = Optional.of(appUserRepo.findUserByUsername(username)).orElseThrow(()->new EntityNotFoundException("no such entity"));
        if(!SecurityCheck.isAdmin() && !SecurityCheck.isOwner(au.getUsername())){
            throw new AccessDeniedException("You are neither the admin or owner of this resource");
        }
            au.setIsEnabled(true);
            au.setResetPassword(true);
            au.setPassword(passwordEncoder.encode(randomPassword));
            appUserRepo.save(au);

        emailSender.sendResetPasswordCredentials(au, randomPassword);

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
    public AppUserDto completeRegistration(AppUser user, ResgistrationCompletionDto registrationCompletionDto) {
//        var attach = attachementRepo.findByAttachmentName(registrationCompletionDto.getImageUrl().getAttachmentName())
//                .orElse(
//                        attachementRepo.save(registrationCompletionDto.getImageUrl())
//                );
//        log.info(attach.getAttachmentName());
        user.setWebsite(registrationCompletionDto.getWebsite());
        user.setImageUrl(registrationCompletionDto.getImageUrl());
        user.setNickname(registrationCompletionDto.getNickname());
        user.setDateOfBirth(registrationCompletionDto.getDateOfBirth());
        user.setAddress(registrationCompletionDto.getAddress());
        user.setRegistrationCompleted(true);

        var completedUser = appUserRepo.save(user);

        log.info("Is user registration complete: "+completedUser.isRegistrationCompleted());

        return mapper.mapToAppUserDto(completedUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
           return Optional.of(appUserRepo.findUserByUsername(username))
                    .orElseThrow(()->new UsernameNotFoundException("User not found!"));

    }


    @Override
    public String updateUser(AppUser user, UpdateUserRequest registrationCompletionDto) throws EntityNotFoundException {
        var att = attachementRepo.findByAttachmentName(registrationCompletionDto.getImageUrl().getAttachmentName()).orElseThrow(
                ()->new EntityNotFoundException("Image Does not exist")
        );

        user.setWebsite(registrationCompletionDto.getWebsite());
        user.setImageUrl(att);
        user.setNickname(registrationCompletionDto.getNickname());
        user.setDateOfBirth(registrationCompletionDto.getDateOfBirth());
        user.setAddress(registrationCompletionDto.getAddress());
        user.setRegistrationCompleted(true);

        var completedUser = appUserRepo.save(user);

        return "user details successfully modified";
    }

    @Override
    public String newPassword(Map<String, String> newPassword) throws EntityNotFoundException {
        var username = newPassword.get("username");
        var password = newPassword.get("password");

        var user = Optional.of(appUserRepo.findUserByUsername(username)).orElseThrow( ()->new EntityNotFoundException("No such user.."));
        user.setPassword(passwordEncoder.encode(password));
        user.setResetPassword(false);

        appUserRepo.save(user);


        return "password successully saved";
    }

    private  String generateCode() {
        Random r = new Random( System.currentTimeMillis() );
        var x = ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));

        return String.valueOf(x);
    }




}
