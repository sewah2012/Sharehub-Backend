package com.luslusdawmpfe.PFEBackent.controllers;

import com.luslusdawmpfe.PFEBackent.dtos.ApiResponseDto;
import com.luslusdawmpfe.PFEBackent.exceptions.IllegalFileEextensionException;
import com.luslusdawmpfe.PFEBackent.services.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/storage")
public class StorageController {
    private final StorageService service;

    @PreAuthorize("hasAnyAuthority({'APP_USER','APP_ADMIN'})")
    @PostMapping("/upload/image")
    ResponseEntity<ApiResponseDto> storeImage(@RequestParam("file") MultipartFile multipartFile) throws IllegalFileEextensionException {
        return ResponseEntity.ok(service.uploadImage(multipartFile));
    }

    @PreAuthorize("hasAnyAuthority({'APP_USER','APP_ADMIN'})")
    @PostMapping("/uploadResource")
    ResponseEntity<ApiResponseDto> uploadResource(@RequestParam("files") MultipartFile[] files) throws IllegalFileEextensionException {
        return ResponseEntity.ok(service.uploadFiles(files));
    }

    @PreAuthorize("hasAnyAuthority({'APP_USER','APP_ADMIN'})")
    @PostMapping("/deleteResource")
    ResponseEntity<ApiResponseDto> storeImage(@RequestParam("fileName") String filename) throws Exception {
        return ResponseEntity.ok(service.deleteResource(filename));
    }
}
