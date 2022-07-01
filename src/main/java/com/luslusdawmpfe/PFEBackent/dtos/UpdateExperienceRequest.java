package com.luslusdawmpfe.PFEBackent.dtos;

import com.luslusdawmpfe.PFEBackent.entities.ExperienceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateExperienceRequest {
    private UUID id;
    private ExperienceType experienceType;
    private String title;
    private String details;
}
