package com.luslusdawmpfe.PFEBackent.dtos;

import com.luslusdawmpfe.PFEBackent.entities.AppUser;
import com.luslusdawmpfe.PFEBackent.entities.Attachement;
import com.luslusdawmpfe.PFEBackent.entities.Comment;
import com.luslusdawmpfe.PFEBackent.entities.ExperienceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExperienceDto {

    private Long id;
    private ExperienceType experienceType;
    private String title;
    private String details;
    private String author;
    private List<Comment> comments = new ArrayList<>();
    private List<Attachement> attachments = new ArrayList<>();
    private Set<String> likes = new HashSet<>();
    private Boolean isActive = false;
    private LocalDateTime creationDate;
    private LocalDateTime updatedDate;
}
