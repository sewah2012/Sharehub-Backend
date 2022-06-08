package com.luslusdawmpfe.PFEBackent.mappers;

import com.luslusdawmpfe.PFEBackent.dtos.RoleDto;
import com.luslusdawmpfe.PFEBackent.entities.Role;
import org.mapstruct.Mapper;

@Mapper(config=MapperConfiguration.class)
public interface RoleMapper {
    Role roleDtoToRole(RoleDto roleDto);
    RoleDto roleToRoleDto(Role role);
}
