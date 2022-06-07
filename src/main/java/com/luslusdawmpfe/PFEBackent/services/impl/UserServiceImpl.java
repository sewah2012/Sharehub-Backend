package com.luslusdawmpfe.PFEBackent.services.impl;

import com.luslusdawmpfe.PFEBackent.dtos.AppUserDto;
import com.luslusdawmpfe.PFEBackent.dtos.CreateUserDto;
import com.luslusdawmpfe.PFEBackent.entities.AppUser;
import com.luslusdawmpfe.PFEBackent.entities.Role;
import com.luslusdawmpfe.PFEBackent.mappers.AppUserMapper;
import com.luslusdawmpfe.PFEBackent.repos.AppUserRepo;
import com.luslusdawmpfe.PFEBackent.services.UserService;
import com.luslusdawmpfe.PFEBackent.utils.EmailSender;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    private EmailSender emailSender;

    @Override
    public String addNewUser(CreateUserDto createUserDto, String siteUrl) throws Exception {

        if(appUserRepo.findUserByEmail(createUserDto.getEmail()).isPresent()) throw new Exception("User already existing!");

        String encryptedPassword = passwordEncoder.encode(createUserDto.getPassword());
        log.info("saving user to db");
       AppUser user = appUserRepo.save(AppUser.builder()
                .username(createUserDto.getUsername())
                .password(encryptedPassword)
                .firstName(createUserDto.getFirstName())
                .lastName(createUserDto.getLastName())
                .nickname(createUserDto.getNickname())
                .dateOfBirth(createUserDto.getDateOfBirth())
                .address(createUserDto.getAddress())
                .email(createUserDto.getEmail())
                .website(createUserDto.getWebsite())
                .imageUrl(createUserDto.getImageUrl())
                        .verificationCode(RandomString.make(64))
                        .isEnabled(false)
                .roles(Set.of(Role.builder().name("APP_ADMIN").description("Admin User").build()))
                .build()
        );
        log.info("initiating email sending");
        emailSender.sendEmail(user, siteUrl);
        return "A activation email has been sent to your email address.";
    }

    @Override
    public ResponseEntity<AppUserDto> getUser(Long userId) throws Exception {
        AppUserDto user = appUserRepo.findById(userId)
                .map((x)->mapper.mapToAppUserDto(x))
                .orElseThrow(()->new Exception("User not found!"));
        return ResponseEntity.ok(user);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
            AppUser user = Optional.of(appUserRepo.findUserByUsername(username))
                    .orElseThrow(()->new UsernameNotFoundException("User not found!"));

        List<SimpleGrantedAuthority> roles = user.getRoles()
                .stream().map(role -> new SimpleGrantedAuthority(role.toString())).collect(Collectors.toList());

        return new User(
                user.getUsername(),
                user.getPassword(),
                user.getIsEnabled(),
                false,
                false,
                false,
                roles
        );
    }




}
