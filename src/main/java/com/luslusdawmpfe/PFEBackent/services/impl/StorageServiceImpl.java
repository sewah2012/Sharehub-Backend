package com.luslusdawmpfe.PFEBackent.services.impl;

import com.google.gson.Gson;
import com.luslusdawmpfe.PFEBackent.dtos.ApiResponseDto;
import com.luslusdawmpfe.PFEBackent.dtos.AttachementDto;
import com.luslusdawmpfe.PFEBackent.entities.Attachement;
import com.luslusdawmpfe.PFEBackent.entities.AttachementType;
import com.luslusdawmpfe.PFEBackent.exceptions.EntityNotFoundException;
import com.luslusdawmpfe.PFEBackent.exceptions.IllegalFileEextensionException;
import com.luslusdawmpfe.PFEBackent.repos.AttachementRepo;
import com.luslusdawmpfe.PFEBackent.services.StorageService;
import com.luslusdawmpfe.PFEBackent.utils.FileUploadHelpers;
//import com.luslusdawmpfe.PFEBackent.utils.fileUploadHelpers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final AttachementRepo attachementRepo;
    @Autowired
    private FileUploadHelpers fileUploadHelpers;
    @Override
    public ApiResponseDto uploadImage(MultipartFile multipartFile) throws Exception {

        var rsp =  uploadOneImage(multipartFile);
//        var att = attachementRepo.save(
//                Attachement.builder()
//                        .attachmentName((String)rsp.get("filename"))
//                        .attachmentUrl((String)rsp.get("url"))
//                        .type(AttachementType.IMAGE)
//                        .build()
//        );
        log.info("RESPONSE {}",new Gson().toJson(rsp));
        return ApiResponseDto.builder().response(rsp).build();
    }

    @Override
    public ApiResponseDto uploadVideo(MultipartFile multipartFile) throws Exception {

        //TODO: check video is not more than 1 min

        var fileExtension = fileUploadHelpers.getExtension(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        Set<String> acceptedFileExtension =Set.of(".mp4");
        if(!acceptedFileExtension.contains(fileExtension)) throw new IllegalFileEextensionException("Invalid File Type: Please use Video "+acceptedFileExtension +"extension");

        String VIDEO_TEMP_URL;
        String filename;
        try {
            filename = multipartFile.getOriginalFilename();
            assert filename != null; //me giving java assurance that file name will always be available
            filename = UUID.randomUUID().toString().concat(fileExtension);

            File file = fileUploadHelpers.convertToFile(multipartFile,filename);
            VIDEO_TEMP_URL = fileUploadHelpers.uploadFile(file, filename);
            var x = file.delete();

            log.info("File name uploaded: "+filename);
            Map<String, Object> rsp = new HashMap<>();
            rsp.put("filename", filename);
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
       var isDeleted= fileUploadHelpers.deleteFile(filename);

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
                        var extension = fileUploadHelpers.getExtension(Objects.requireNonNull(file.getOriginalFilename()));
                        if(imageExtentions.contains(extension)){
                            try {
                                return this.uploadImage(file).getResponse();

                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }

                        if(videoExtentions.contains(extension)) {
                            try {
                                return this.uploadImage(file).getResponse();

                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                        return null;
                      }).collect(Collectors.toList());


        Map<String, Object> rsp = new HashMap<>();
        rsp.put("attachements", data);



        return ApiResponseDto.builder().response(rsp).build();

    }


    @Override
    public ApiResponseDto changeProfilePic(MultipartFile newPic, String attachmentName) throws IllegalFileEextensionException, IOException {
       var rsp =  uploadOneImage(newPic);

        var att = attachementRepo.findByAttachmentName(attachmentName)
                .orElse(
                        Attachement.builder()
                                .attachmentName((String)rsp.get("filename"))
                                .attachmentUrl((String)rsp.get("url"))
                                .type(AttachementType.IMAGE)
                                .build()
                );

        //update the attachement
        att.setAttachmentName((String)rsp.get("filename"));
        att.setAttachmentUrl((String)rsp.get("url"));
        attachementRepo.save(att);

        //delete the old image from firebase
        var isDeleted= fileUploadHelpers.deleteFile(attachmentName);



        return ApiResponseDto.builder().response(rsp).build();
    }


    private Map<String, Object> uploadOneImage(MultipartFile multipartFile) throws IllegalFileEextensionException {
        var fileExtension = fileUploadHelpers.getExtension(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        Set<String> acceptedFileExtension =Set.of(".png",".jpg",".jpeg");
        if(!acceptedFileExtension.contains(fileExtension)) throw new IllegalFileEextensionException("Invalid File Type: Please use an image of either "+acceptedFileExtension +"extension");
        String filename;
        String TEMP_URL;
        try {
            filename = multipartFile.getOriginalFilename();
            assert filename != null; //me giving java assurance that file name will always be available
            filename = UUID.randomUUID().toString().concat(fileExtension);

            File file = fileUploadHelpers.convertToFile(multipartFile,filename);
            TEMP_URL = fileUploadHelpers.uploadFile(file, filename);
            var x = file.delete();

            Map<String, Object> rsp = new HashMap<>();
            log.info("File name uploaded: "+filename);
            rsp.put("filename", filename);
            rsp.put("url", TEMP_URL);
            rsp.put("type", AttachementType.IMAGE);

            return rsp;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

