package com.luslusdawmpfe.PFEBackent.mappers;

import com.luslusdawmpfe.PFEBackent.dtos.AppUserDto;
import com.luslusdawmpfe.PFEBackent.entities.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.context.annotation.Lazy;

//@Lazy
@Mapper(componentModel = "spring")
public interface AppUserMapper {

    AppUserDto mapToAppUserDto(AppUser user);
//    AppUser mapToAppUser(AppUserDto user);
//    @Mapping(target = "author", expression = "java(user.getUsername())")
//    String mapToAppUser(AppUserDto user);
}
