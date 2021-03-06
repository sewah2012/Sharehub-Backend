package com.luslusdawmpfe.PFEBackent.repos;

import com.luslusdawmpfe.PFEBackent.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppUserRepo extends JpaRepository<AppUser, UUID> {

    public AppUser findUserByUsername(String username);

    Optional<AppUser> findUserByEmail(String email);

    Optional<AppUser> findUserByVerificationCode(String verificationCode);
}
