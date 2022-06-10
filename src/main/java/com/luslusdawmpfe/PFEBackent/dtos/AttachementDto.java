package com.luslusdawmpfe.PFEBackent.dtos;

import com.luslusdawmpfe.PFEBackent.entities.AttachementType;
import com.luslusdawmpfe.PFEBackent.entities.Experience;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachementDto {
    private Long id;
    private AttachementType type;
    private String attachmentUrl;
}
