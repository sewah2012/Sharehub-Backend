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
//                                .imageUrl(Attachement.builder()
//                                        .attachmentName("usr1.jpeg")
//                                        .attachmentUrl("https://firebasestorage.googleapis.com/v0/b/share-hub-4607b.appspot.com/o/usr1.jpeg?alt=media&token=9552e3e0-3e2f-4aec-8fb6-3dfeede71b16")
//                                        .type(AttachementType.IMAGE)
//                                        .build()
//                                )
                                .isRegistrationCompleted(false)
                                .isEnabled(true)
                                .roles(List.of(new Role(null,"APP_ADMIN","App adminsitrator")))
                                .build()
                );
            }
        };
    }
}
