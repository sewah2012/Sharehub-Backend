package com.luslusdawmpfe.PFEBackent.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class SignupDto {
    private final String firstName;
    private final String lastName;
    private final String username;
    private final String password;
    private final String email;
}
