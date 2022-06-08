package com.luslusdawmpfe.PFEBackent.services.impl;

import com.luslusdawmpfe.PFEBackent.dtos.RoleDto;
import com.luslusdawmpfe.PFEBackent.mappers.RoleMapper;
import com.luslusdawmpfe.PFEBackent.repos.RoleRepo;
import com.luslusdawmpfe.PFEBackent.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepo roleRepo;
    private final RoleMapper roleMapper;

    @Override
    public String addNewRole(RoleDto role) throws Exception {
        if(roleRepo.findById(role.getId()).isPresent()) throw new Exception("Role already exist!");
        roleRepo.save(roleMapper.roleDtoToRole(role));
        return "Role successfully Added";
    }

    @Override
    public List<RoleDto> getAllRoles() {
        return roleRepo.findAll().stream().map(roleMapper::roleToRoleDto).collect(Collectors.toList());
    }

    @Override
    public RoleDto getSingleRole(Long roleId) throws Exception {
        return roleMapper.roleToRoleDto(roleRepo.findById(roleId).orElseThrow(()-> new Exception("Role not found")));

    }

    @Override
    public String editRole(RoleDto role) throws Exception {
        roleRepo.findById(role.getId())
                .map((x)->roleRepo.save(roleMapper.roleDtoToRole(role)))
                .orElseThrow(()->new Exception("Role does not exist!"));
        return "Role successfully modified!";
    }

    @Override
    public String deleteRole(Long roleId) throws Exception {
        roleRepo.delete(roleRepo.findById(roleId).orElseThrow(()-> new Exception("Role not found")));
        return "role deleted successfully";
    }
}
