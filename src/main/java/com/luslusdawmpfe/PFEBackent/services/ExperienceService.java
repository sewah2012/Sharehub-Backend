package com.luslusdawmpfe.PFEBackent.services;

import com.luslusdawmpfe.PFEBackent.dtos.AddExperienceDto;
import com.luslusdawmpfe.PFEBackent.dtos.ExperienceDto;
import com.luslusdawmpfe.PFEBackent.entities.AppUser;
import com.luslusdawmpfe.PFEBackent.entities.Experience;
import com.luslusdawmpfe.PFEBackent.exceptions.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ExperienceService {
    String shareExperience(AddExperienceDto experience, AppUser user);
    ResponseEntity<List<ExperienceDto>> listExperiences(int pageNumber, int pageSize, String softBy);
    ExperienceDto getOneExperience(Long experienceId) throws EntityNotFoundException;
    String updateExperience(Experience experience) throws EntityNotFoundException;
    String deleteExperience(Long experience) throws EntityNotFoundException;

}