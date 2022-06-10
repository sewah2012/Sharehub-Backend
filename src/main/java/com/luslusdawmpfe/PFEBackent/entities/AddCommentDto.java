package com.luslusdawmpfe.PFEBackent.entities;

import com.luslusdawmpfe.PFEBackent.dtos.ExperienceDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCommentDto {
    private AppUser author;
    private String description;
    private LocalDate dateCreated;
    private ExperienceDto experience;
}
