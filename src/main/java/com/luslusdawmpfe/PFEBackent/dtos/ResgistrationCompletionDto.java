package com.luslusdawmpfe.PFEBackent.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResgistrationCompletionDto {
    private String website;
    private String imageUrl;
    private String nickname;
    private LocalDate dateOfBirth;
    private String address;
}
