package com.luslusdawmpfe.PFEBackent.loaders;

import com.luslusdawmpfe.PFEBackent.entities.AppUser;
import com.luslusdawmpfe.PFEBackent.entities.Attachement;
import com.luslusdawmpfe.PFEBackent.entities.AttachementType;
import com.luslusdawmpfe.PFEBackent.entities.Role;
import com.luslusdawmpfe.PFEBackent.repos.AppUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@Lazy
@Configuration
@RequiredArgsConstructor
public class AdminLoader {
    private final AppUserRepo appUserRepo;
    private final PasswordEncoder passwordEncoder;
    @Bean
    ApplicationRunner loadAdminUser(){
        return args ->{
            if(appUserRepo.findUserByUsername("Admin")==null){
                appUserRepo.save(
                        AppUser.builder()
                                .username("Admin")
                                .password(passwordEncoder.encode("admin"))
                                .firstName("Emmanuel")
                                .lastName("Sewah")
                                .nickname("easy_SWAP")
                                .dateOfBirth(LocalDate.of(1998,8,22))
                                .address("Sidi Maarouf, Casablanca")
                                .email("sewah2012@gmail.com")
                                .website("www.emmanuelsewah.ga")
                                .imageUrl(Attachement.builder()
                                        .attachmentName("admin Profile picture")
                                        .attachmentUrl("https://media-exp1.licdn.com/dms/image/C5603AQF_i_yDKlZ85g/profile-displayphoto-shrink_200_200/0/1630420188712?e=1660176000&v=beta&t=1YtVFJpenpzhpEevdI_JQJkHGBOodwjrPcctk2PU2N4")
                                        .type(AttachementType.IMAGE)
                                        .build()
                                )
                                .isRegistrationCompleted(true)
                                .isEnabled(true)
                                .roles(List.of(new Role(null,"APP_ADMIN","App adminsitrator")))
                                .build()
                );
            }
        };
    }
}
