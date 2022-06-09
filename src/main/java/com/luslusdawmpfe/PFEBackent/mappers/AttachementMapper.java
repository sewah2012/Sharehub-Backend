package com.luslusdawmpfe.PFEBackent.mappers;

import com.luslusdawmpfe.PFEBackent.dtos.AttachementDto;
import com.luslusdawmpfe.PFEBackent.entities.Attachement;
import org.mapstruct.Mapper;

@Mapper(config=MapperConfiguration.class)
public interface AttachementMapper {
    Attachement attachementDtoToAttachement(AttachementDto a);
}
