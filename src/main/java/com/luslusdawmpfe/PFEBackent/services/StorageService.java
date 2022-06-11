package com.luslusdawmpfe.PFEBackent.services;

import com.luslusdawmpfe.PFEBackent.dtos.ApiResponseDto;
import com.luslusdawmpfe.PFEBackent.exceptions.IllegalFileEextensionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    ResponseEntity<ApiResponseDto> uploadImage(MultipartFile multipartFile) throws IllegalFileEextensionException;

    ResponseEntity<ApiResponseDto> deleteResource(String filename) throws Exception;
    ResponseEntity<ApiResponseDto> uploadVideo(MultipartFile multipartFile) throws IllegalFileEextensionException;

}
