package com.luslusdawmpfe.PFEBackent.mappers;

import com.luslusdawmpfe.PFEBackent.dtos.*;
import com.luslusdawmpfe.PFEBackent.entities.AppUser;
import com.luslusdawmpfe.PFEBackent.entities.Comment;
import com.luslusdawmpfe.PFEBackent.entities.Experience;
import com.luslusdawmpfe.PFEBackent.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Mapper(config=MapperConfiguration.class)
public interface ExperienceMapper {
    Experience experienceDtoToExperience(AddExperienceDto ex);

    ExperienceAuthorDto AppUserExperienceAuthorDto(AppUser user);
    ExperienceDto mapToExperienceDto(Experience ex);
}
