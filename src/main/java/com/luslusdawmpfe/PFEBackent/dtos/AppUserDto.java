package com.luslusdawmpfe.PFEBackent.dtos;

import com.luslusdawmpfe.PFEBackent.entities.Attachement;
import com.luslusdawmpfe.PFEBackent.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUserDto implements Serializable {
    private UUID id;
    private  String firstName;
    private  String lastName;
    private  String nickname;
    private  LocalDate dateOfBirth;
    private  String address;
    private  String email;
    private  String website;
    private Attachement imageUrl;
    private  String username;
    private boolean isRegistrationCompleted;
    private  List<Role> roles;

}
