package com.luslusdawmpfe.PFEBackent.entities;

import lombok.*;
import net.bytebuddy.utility.RandomString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class AppUser implements UserDetails, Serializable {
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


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles
                .stream().map(role -> new SimpleGrantedAuthority(role.toString())).collect(Collectors.toList());

    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}
