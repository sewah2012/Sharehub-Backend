package com.luslusdawmpfe.PFEBackent.mappers;

import com.luslusdawmpfe.PFEBackent.dtos.AttachementDto;
import com.luslusdawmpfe.PFEBackent.entities.Attachement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

import static com.google.api.ResourceProto.resource;

@Mapper(config=MapperConfiguration.class)
public interface AttachementMapper {
    Attachement attachementDtoToAttachement(AttachementDto a);

    @Mapping(source="attachmentName", target="attachmentName")
    AttachementDto mapToAttachementDto(Attachement a);



    List<AttachementDto> mapToAttachementDtoList(List<Attachement> attachements);

}
