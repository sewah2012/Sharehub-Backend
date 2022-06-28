package com.luslusdawmpfe.PFEBackent.services;

import com.luslusdawmpfe.PFEBackent.dtos.AddExperienceDto;
import com.luslusdawmpfe.PFEBackent.dtos.ExperienceDto;
import com.luslusdawmpfe.PFEBackent.entities.AppUser;
import com.luslusdawmpfe.PFEBackent.entities.Experience;
import com.luslusdawmpfe.PFEBackent.exceptions.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ExperienceService {
    ExperienceDto shareExperience(MultipartFile[] files, AddExperienceDto experience, AppUser user);
    ResponseEntity<List<ExperienceDto>> listExperiences(int pageNumber, int pageSize, String softBy);
    ExperienceDto getOneExperience(String experienceId) throws EntityNotFoundException;
    String updateExperience(Experience experience) throws EntityNotFoundException;
    String deleteExperience(String experience) throws EntityNotFoundException;

    ResponseEntity<List<ExperienceDto>> listPopularExperiences();
    ResponseEntity<List<ExperienceDto>> listLatestExperiences();

}