package com.luslusdawmpfe.PFEBackent.dtos;

import com.luslusdawmpfe.PFEBackent.entities.ExperienceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
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
    private ExperienceAuthorDto author;
    private List<CommentDto> comments;
    private List<AttachementDto> attachments;
    private Set<String> likes;
    private Boolean isActive = false;
    private OffsetDateTime creationDate;
    private OffsetDateTime updatedDate;
}
