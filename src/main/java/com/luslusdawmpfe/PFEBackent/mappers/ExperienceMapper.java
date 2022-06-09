package com.luslusdawmpfe.PFEBackent.mappers;

import com.luslusdawmpfe.PFEBackent.dtos.AddExperienceDto;
import com.luslusdawmpfe.PFEBackent.dtos.ExperienceDto;
import com.luslusdawmpfe.PFEBackent.entities.Experience;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config=MapperConfiguration.class)
public interface ExperienceMapper {
    Experience experienceDtoToExperience(AddExperienceDto ex);
    @Mapping(target = "author", expression = "java(ex.getAuthor().getUsername())")
    ExperienceDto mapToExperienceDto(Experience ex);

}
