package com.luslusdawmpfe.PFEBackent.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luslusdawmpfe.PFEBackent.dtos.AddExperienceDto;
import com.luslusdawmpfe.PFEBackent.dtos.ExperienceDto;
import com.luslusdawmpfe.PFEBackent.entities.AppUser;
import com.luslusdawmpfe.PFEBackent.entities.Experience;
import com.luslusdawmpfe.PFEBackent.exceptions.EntityNotFoundException;
import com.luslusdawmpfe.PFEBackent.services.ExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/experience")
public class ExperienceController {
    private final ExperienceService experienceService;

    @PreAuthorize("hasAnyAuthority({'APP_ADMIN','APP_USER'})")
    @PostMapping(value="/add")
    public ResponseEntity<ExperienceDto> addNewExperience(@RequestParam("attachements") MultipartFile[] files, @RequestParam("experience") String experience, @AuthenticationPrincipal AppUser user) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        AddExperienceDto obj = mapper.readValue(experience, AddExperienceDto.class);

        return ResponseEntity.ok(experienceService.shareExperience(files, obj, user));
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
    ResponseEntity<ExperienceDto> getOne(@PathVariable("experienceId") Long experienceId) throws EntityNotFoundException {
        return ResponseEntity.ok(experienceService.getOneExperience(experienceId));
    }

    @PutMapping("/edit")
    ResponseEntity<String> updateExperience(@RequestBody Experience experience) throws EntityNotFoundException {
        return ResponseEntity.ok(experienceService.updateExperience(experience));
    }

    @DeleteMapping("/delete/{experienceId}")
    ResponseEntity<String> deleteExperience (@PathVariable("experienceId") Long experience) throws EntityNotFoundException {
        return ResponseEntity.ok(experienceService.deleteExperience(experience));
    }

}
