package com.luslusdawmpfe.PFEBackent.dtos;
import com.luslusdawmpfe.PFEBackent.entities.AppUser;
import com.luslusdawmpfe.PFEBackent.entities.Experience;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private UUID id;
    private CommentAuthorDto author;
    private String description;
    private OffsetDateTime creationDate;
    private OffsetDateTime updatedDate;
//    private Long experienceId;
}
