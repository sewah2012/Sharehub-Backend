package com.luslusdawmpfe.PFEBackent.utils;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize(value = "hasAnyAuthority('APP_ADMIN') " +
        "|| (hasAnyAuthority('APP_USER') && #experience.getAuthor().getUsername().equals(authentication.principal.username))")
public @interface IsExperienceOwnerOrAppAdmin {
}
