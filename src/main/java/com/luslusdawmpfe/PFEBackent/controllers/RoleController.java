package com.luslusdawmpfe.PFEBackent.controllers;

import com.luslusdawmpfe.PFEBackent.dtos.RoleDto;
import com.luslusdawmpfe.PFEBackent.services.RoleService;
import com.luslusdawmpfe.PFEBackent.services.impl.RoleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('APP_ADMIN')")
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/add")
    public ResponseEntity<String> addNewRole(@RequestBody RoleDto role) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.addNewRole(role));
    }

    @PutMapping("/edit")
    public ResponseEntity<String>  editRole(@RequestBody RoleDto role) throws Exception {
        return ResponseEntity.ok(roleService.editRole(role));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<RoleDto>> getAllRoles() throws Exception {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping("/getOne/{roleId}")
    public ResponseEntity<RoleDto> getSingleRole(@PathVariable("roleId") Long roleId) throws Exception {
        return ResponseEntity.ok(roleService.getSingleRole(roleId));
    }

    @DeleteMapping("/delete/{roleId}")
    public ResponseEntity<String> deleteRole(@PathVariable("roleId") Long roleId) throws Exception {
        return ResponseEntity.ok(roleService.deleteRole(roleId));
    }

}
