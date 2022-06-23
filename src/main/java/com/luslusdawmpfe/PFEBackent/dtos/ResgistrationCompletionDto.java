package com.luslusdawmpfe.PFEBackent.dtos;

import com.luslusdawmpfe.PFEBackent.entities.Attachement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResgistrationCompletionDto {
    private String website;
    private Attachement imageUrl;
    private String nickname;
    private LocalDate dateOfBirth;
    private String address;
}
