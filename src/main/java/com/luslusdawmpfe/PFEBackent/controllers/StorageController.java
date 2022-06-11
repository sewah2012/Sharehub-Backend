package com.luslusdawmpfe.PFEBackent.controllers;

import com.luslusdawmpfe.PFEBackent.dtos.ApiResponseDto;
import com.luslusdawmpfe.PFEBackent.exceptions.IllegalFileEextensionException;
import com.luslusdawmpfe.PFEBackent.services.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/storage")
public class StorageController {
    private final StorageService service;

    @PreAuthorize("hasAnyAuthority({'APP_USER','APP_ADMIN'})")
    @PostMapping("/upload/image")
    ResponseEntity<ApiResponseDto> storeImage(@RequestParam("file")MultipartFile multipartFile) throws IllegalFileEextensionException {
        return service.uploadImage(multipartFile);
    }

    @PreAuthorize("hasAnyAuthority({'APP_USER','APP_ADMIN'})")
    @PostMapping("/deleteResource")
    ResponseEntity<ApiResponseDto> storeImage(@RequestParam("fileName") String filename) throws Exception {
        return service.deleteResource(filename);
    }
}
