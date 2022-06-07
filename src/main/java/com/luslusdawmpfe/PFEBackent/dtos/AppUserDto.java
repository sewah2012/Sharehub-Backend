package com.luslusdawmpfe.PFEBackent.dtos;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Data
public class AppUserDto implements Serializable {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String nickname;
    private final LocalDate dateOfBirth;
    private final String address;
    private final String email;
    private final String website;
    private final String imageUrl;
    private final String username;
    private final Set<String> roles;
}
