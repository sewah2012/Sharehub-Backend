package com.luslusdawmpfe.PFEBackent.dtos;

import com.luslusdawmpfe.PFEBackent.entities.ExperienceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddExperienceDto {
    private ExperienceType experienceType;
    private String title;
    private String details;
//    private List<MultipartFile> attachements;
//    private List<AttachementDto> attachments = new ArrayList<>();
}
