package com.luslusdawmpfe.PFEBackent.controllers;

import com.luslusdawmpfe.PFEBackent.dtos.AddExperienceDto;
import com.luslusdawmpfe.PFEBackent.dtos.ExperienceDto;
import com.luslusdawmpfe.PFEBackent.entities.AppUser;
import com.luslusdawmpfe.PFEBackent.entities.Experience;
import com.luslusdawmpfe.PFEBackent.exceptions.EntityNotFoundException;
import com.luslusdawmpfe.PFEBackent.services.ExperienceService;
import com.luslusdawmpfe.PFEBackent.utils.IsExperienceOwnerOrAppAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/experience")
public class ExperienceController {
    private final ExperienceService experienceService;

    @PreAuthorize("hasAnyAuthority({'APP_ADMIN','APP_USER'})")
    @PostMapping("/add")
    public ResponseEntity<String> addNewExperience(@RequestBody AddExperienceDto experience, @AuthenticationPrincipal AppUser user){
        return ResponseEntity.ok(experienceService.shareExperience(experience, user));
    }

    @PreAuthorize("hasAnyAuthority({'APP_ADMIN','APP_USER'})")
    @GetMapping("/list")
    public ResponseEntity<List<ExperienceDto>>listExperiences(
            @RequestParam(name="pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(name="pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(name="sort", required = false, defaultValue = "creationDate") String sortBy

    ){
        return experienceService.listExperiences(pageNumber,pageSize,sortBy);
    }

    @PreAuthorize("hasAnyAuthority({'APP_ADMIN','APP_USER'})")
    @GetMapping("/getOne/{experienceId}")
    ResponseEntity<Experience> getOne(@PathVariable("experienceId") Long experienceId) throws EntityNotFoundException {
        return ResponseEntity.ok(experienceService.getOneExperience(experienceId));
    }

    @IsExperienceOwnerOrAppAdmin
    @PutMapping("/edit")
    ResponseEntity<String> updateExperience(@RequestBody Experience experience) throws EntityNotFoundException {
        return ResponseEntity.ok(experienceService.updateExperience(experience));
    }

    @IsExperienceOwnerOrAppAdmin
    @DeleteMapping("/delete")
    ResponseEntity<String> deleteExperience (@RequestBody Experience experience) throws EntityNotFoundException {
        return ResponseEntity.ok(experienceService.deleteExperience(experience));
    }




}
