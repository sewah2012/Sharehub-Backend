package com.luslusdawmpfe.PFEBackent.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.List;

public class SecurityCheck {
    static Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    static Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    static String loggedInuser = authentication.getName();

    public static Boolean isAdmin(){
        return authorities.contains(new SimpleGrantedAuthority("APP_ADMIN"));
    }

    public static Boolean isOwner(String username){
        return loggedInuser.equals(username);
    }
}
