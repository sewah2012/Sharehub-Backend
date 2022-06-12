package com.luslusdawmpfe.PFEBackent.services.impl;

import com.luslusdawmpfe.PFEBackent.dtos.ApiResponseDto;
import com.luslusdawmpfe.PFEBackent.dtos.AttachementDto;
import com.luslusdawmpfe.PFEBackent.entities.AttachementType;
import com.luslusdawmpfe.PFEBackent.exceptions.IllegalFileEextensionException;
import com.luslusdawmpfe.PFEBackent.services.StorageService;
import com.luslusdawmpfe.PFEBackent.utils.FileUploadHelpers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageServiceImpl implements StorageService {

    @Override
    public ApiResponseDto uploadImage(MultipartFile multipartFile) throws IllegalFileEextensionException {

        //TODO: check if image size is not more than 2mb

        var fileExtension = FileUploadHelpers.getExtension(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        Set<String> acceptedFileExtension =Set.of(".png",".jpg",".jpeg");
        if(!acceptedFileExtension.contains(fileExtension)) throw new IllegalFileEextensionException("Invalid File Type: Please use an image of either "+acceptedFileExtension +"extension");
        String filename;
        String TEMP_URL;
        try {
            filename = multipartFile.getOriginalFilename();
            assert filename != null; //me giving java assurance that file name will always be available
            filename = UUID.randomUUID().toString().concat(fileExtension);

            File file = FileUploadHelpers.convertToFile(multipartFile,filename);
            TEMP_URL = FileUploadHelpers.uploadFile(file, filename);
            var x = file.delete();

            Map<String, Object> rsp = new HashMap<>();
            log.info("File name uploaded: "+filename);
            rsp.put("filename", filename);
            rsp.put("url", TEMP_URL);
            rsp.put("type", AttachementType.IMAGE);

            return ApiResponseDto.builder().response(rsp).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ApiResponseDto uploadVideo(MultipartFile multipartFile) throws IllegalFileEextensionException {

        //TODO: check video is not more than 1 min

        var fileExtension = FileUploadHelpers.getExtension(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        Set<String> acceptedFileExtension =Set.of(".mp4");
        if(!acceptedFileExtension.contains(fileExtension)) throw new IllegalFileEextensionException("Invalid File Type: Please use Video "+acceptedFileExtension +"extension");

        String VIDEO_TEMP_URL;
        String filename;
        try {
            filename = multipartFile.getOriginalFilename();
            assert filename != null; //me giving java assurance that file name will always be available
            filename = UUID.randomUUID().toString().concat(fileExtension);

            File file = FileUploadHelpers.convertToFile(multipartFile,filename);
            VIDEO_TEMP_URL = FileUploadHelpers.uploadFile(file, filename);
            var x = file.delete();

            log.info("File name uploaded: "+filename);
            Map<String, Object> rsp = new HashMap<>();
            rsp.put("fileName", filename);
            rsp.put("url", VIDEO_TEMP_URL);
            rsp.put("type", AttachementType.VIDEO);

            return ApiResponseDto.builder().response(rsp).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ApiResponseDto deleteResource(String filename) throws Exception {
        log.info("Starting to delete a resource with file name of "+filename);
       var isDeleted= FileUploadHelpers.deleteFile(filename);

        Map<String, Object> rsp = new HashMap<>();

        if(! isDeleted) throw new Exception("Failed to delete file");

        rsp.put("msg", "File successfully Deleted");
        return ApiResponseDto.builder().response(rsp).build();
    }
    @Override
    public ApiResponseDto uploadFiles(MultipartFile[] files) {
        Set<String> imageExtentions = Set.of(".jpg",".png",".jpeg");
        Set<String> videoExtentions = Set.of(".mp4");



       var data =  Arrays.stream(files)
                        .map(file->{
                        var extension = FileUploadHelpers.getExtension(Objects.requireNonNull(file.getOriginalFilename()));
                        if(imageExtentions.contains(extension)){
                            try {
                                return this.uploadImage(file).getResponse();

                            } catch (IllegalFileEextensionException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        if(videoExtentions.contains(extension)) {
                            try {
                                return this.uploadImage(file).getResponse();

                            } catch (IllegalFileEextensionException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        return null;
                      }).collect(Collectors.toList());


        Map<String, Object> rsp = new HashMap<>();
        rsp.put("attachements", data);



        return ApiResponseDto.builder().response(rsp).build();

    }
}
