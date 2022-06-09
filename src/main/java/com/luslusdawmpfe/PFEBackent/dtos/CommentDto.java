package com.luslusdawmpfe.PFEBackent.dtos;

import com.luslusdawmpfe.PFEBackent.entities.AppUser;
import com.luslusdawmpfe.PFEBackent.entities.Experience;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private AppUser author;
    private String description;
    private LocalDate dateCreated;
    private Experience experience;
}
