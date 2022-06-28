package com.luslusdawmpfe.PFEBackent;

import com.luslusdawmpfe.PFEBackent.entities.AppUser;
import com.luslusdawmpfe.PFEBackent.entities.Attachement;
import com.luslusdawmpfe.PFEBackent.entities.AttachementType;
import com.luslusdawmpfe.PFEBackent.entities.Role;
import com.luslusdawmpfe.PFEBackent.repos.AppUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
@PropertySource("classpath:application-${spring.profiles.active:default}.yml")
@RequiredArgsConstructor
public class PfeBackentApplication {
	public static void main(String[] args) {
		SpringApplication.run(PfeBackentApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	String stringBean(){
		return new String();
	}
}
