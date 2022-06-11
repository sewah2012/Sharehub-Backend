package com.luslusdawmpfe.PFEBackent.services.impl;

import com.luslusdawmpfe.PFEBackent.dtos.ApiResponseDto;
import com.luslusdawmpfe.PFEBackent.exceptions.IllegalFileEextensionException;
import com.luslusdawmpfe.PFEBackent.services.StorageService;
import com.luslusdawmpfe.PFEBackent.utils.FileUploadHelpers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageServiceImpl implements StorageService {


    @Override
    public ResponseEntity<ApiResponseDto> uploadImage(MultipartFile multipartFile) throws IllegalFileEextensionException {
        var fileExtension = FileUploadHelpers.getExtension(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        Set<String> acceptedFileExtension =Set.of(".png",".jpg",".jpeg");
        if(!acceptedFileExtension.contains(fileExtension)) throw new IllegalFileEextensionException("Invalid File Type: Please use an image of either "+acceptedFileExtension +"extension");

        String TEMP_URL;
        try {
            String filename = multipartFile.getOriginalFilename();
            assert filename != null; //me giving java assurance that file name will always be available
            filename = UUID.randomUUID().toString().concat(fileExtension);

            File file = FileUploadHelpers.convertToFile(multipartFile,filename);
            TEMP_URL = FileUploadHelpers.uploadFile(file, filename);
            var x = file.delete();

            Map<String, Object> rsp = new HashMap<>();
            rsp.put("fileName", filename);
            rsp.put("url", TEMP_URL);

            return ResponseEntity.ok(ApiResponseDto.builder().response(rsp).build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<ApiResponseDto> uploadVideo(MultipartFile multipartFile) throws IllegalFileEextensionException {
        var fileExtension = FileUploadHelpers.getExtension(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        Set<String> acceptedFileExtension =Set.of(".mp4");
        if(!acceptedFileExtension.contains(fileExtension)) throw new IllegalFileEextensionException("Invalid File Type: Please use Video "+acceptedFileExtension +"extension");

        String VIDEO_TEMP_URL;
        try {
            String filename = multipartFile.getOriginalFilename();
            assert filename != null; //me giving java assurance that file name will always be available
            filename = UUID.randomUUID().toString().concat(fileExtension);

            File file = FileUploadHelpers.convertToFile(multipartFile,filename);
            VIDEO_TEMP_URL = FileUploadHelpers.uploadFile(file, filename);
            var x = file.delete();

            Map<String, Object> rsp = new HashMap<>();
            rsp.put("fileName", filename);
            rsp.put("Url", VIDEO_TEMP_URL);

            return ResponseEntity.ok(ApiResponseDto.builder().response(rsp).build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<ApiResponseDto> deleteResource(String filename) throws Exception {
        log.info("Starting to delete a resource with file name of "+filename);
       var isDeleted= FileUploadHelpers.deleteFile(filename);

        Map<String, Object> rsp = new HashMap<>();

        if(! isDeleted) throw new Exception("Failed to delete file");

        rsp.put("msg", "File successfully Deleted");
        return ResponseEntity.ok(ApiResponseDto.builder().response(rsp).build());
    }

}
