package com.luslusdawmpfe.PFEBackent.services;

import com.luslusdawmpfe.PFEBackent.dtos.ApiResponseDto;
import com.luslusdawmpfe.PFEBackent.exceptions.IllegalFileEextensionException;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface StorageService {

    ApiResponseDto uploadImage(MultipartFile multipartFile) throws Exception;

    ApiResponseDto deleteResource(String filename) throws Exception;

    ApiResponseDto uploadFiles(MultipartFile[] files);
    ApiResponseDto uploadVideo(MultipartFile multipartFile) throws Exception;

    ApiResponseDto changeProfilePic(MultipartFile newPic, String attachmentName) throws IllegalFileEextensionException, IOException;
}
