package com.luslusdawmpfe.PFEBackent.services;

import com.luslusdawmpfe.PFEBackent.dtos.ExperienceDto;
import com.luslusdawmpfe.PFEBackent.entities.AppUser;
import com.luslusdawmpfe.PFEBackent.entities.Experience;
import com.luslusdawmpfe.PFEBackent.exceptions.EntityNotFoundException;
import org.springframework.data.domain.Page;

public interface ExperienceService {
    String shareExperience(ExperienceDto experience, AppUser user);
    Page<Experience> listExperiences(int pageNumber, int pageSize, String softBy);
    Experience getOneExperience(Long experienceId) throws EntityNotFoundException;
    String updateExperience(Experience experience) throws EntityNotFoundException;
    String deleteExperience(Experience experience) throws EntityNotFoundException;

}