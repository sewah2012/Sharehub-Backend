package com.luslusdawmpfe.PFEBackent.mappers;

import com.luslusdawmpfe.PFEBackent.dtos.ExperienceDto;
import com.luslusdawmpfe.PFEBackent.entities.Experience;
import org.mapstruct.Mapper;

@Mapper(config=MapperConfiguration.class)
public interface ExperienceMapper {
    Experience experienceDtoToExperience(ExperienceDto ex);

}
