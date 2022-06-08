package com.luslusdawmpfe.PFEBackent.services;

import com.luslusdawmpfe.PFEBackent.dtos.RoleDto;

import java.util.List;

public interface RoleService {
    public String addNewRole(RoleDto role) throws Exception;
    public List<RoleDto> getAllRoles();
    public RoleDto getSingleRole(Long roleId) throws Exception;
    public String editRole(RoleDto role) throws Exception;
    public String deleteRole(Long roleId) throws Exception;


}
