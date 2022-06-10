package com.luslusdawmpfe.PFEBackent.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentAuthorDto {
    private  Long id;
    private  String firstName;
    private  String lastName;
    private  String imageUrl;
    private  String username;
}
