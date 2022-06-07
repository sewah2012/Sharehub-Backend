package com.luslusdawmpfe.PFEBackent.entities;

import lombok.*;
import net.bytebuddy.utility.RandomString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class AppUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String nickname;
    private LocalDate dateOfBirth;
    private String address;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    private String website;
    private String imageUrl;
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    private String password;
    @Column(name = "verification_code", length =64)
    private String verificationCode;
    private Boolean isEnabled;

//    @Builder.Default
//    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Role> roles = new HashSet<>();


}
