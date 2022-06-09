package com.luslusdawmpfe.PFEBackent;

import com.luslusdawmpfe.PFEBackent.entities.AppUser;
import com.luslusdawmpfe.PFEBackent.entities.Role;
import com.luslusdawmpfe.PFEBackent.repos.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class PfeBackentApplication {
@Autowired
private AppUserRepo appUserRepo;
	public static void main(String[] args) {
		SpringApplication.run(PfeBackentApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner buildAdminUser(){
		return (args)-> {
			appUserRepo.save(
					AppUser.builder()
							.username("Admin")
							.password(passwordEncoder().encode("admin"))
							.firstName("Emmanuel")
							.lastName("Sewah")
							.nickname("easy_SWAP")
							.dateOfBirth(LocalDate.of(1998,8,22))
							.address("Sidi Maarouf, Casablanca")
							.email("sewah2012@gmail.com")
							.website("www.emmanuelsewah.ga")
							.imageUrl("")
							.isProfileComplete(true)
							.isEnabled(true)
							.roles(List.of(new Role(null,"APP_ADMIN","App adminsitrator")))
							.build()
			);
		};
	}
}
