package com.luslusdawmpfe.PFEBackent.dtos;

import com.luslusdawmpfe.PFEBackent.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUserDto implements Serializable {
    private  Long id;
    private  String firstName;
    private  String lastName;
    private  String nickname;
    private  LocalDate dateOfBirth;
    private  String address;
    private  String email;
    private  String website;
    private  String imageUrl;
    private  String username;
    private  List<Role> roles;

    public AppUserDto(Long id, String firstName, String lastName, String imageUrl, String username) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageUrl = imageUrl;
        this.username = username;
    }
}
