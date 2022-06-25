package com.luslusdawmpfe.PFEBackent.services.impl;
import com.luslusdawmpfe.PFEBackent.dtos.AddExperienceDto;
import com.luslusdawmpfe.PFEBackent.dtos.ApiResponseDto;
import com.luslusdawmpfe.PFEBackent.dtos.ExperienceDto;
import com.luslusdawmpfe.PFEBackent.entities.AppUser;
import com.luslusdawmpfe.PFEBackent.entities.Attachement;
import com.luslusdawmpfe.PFEBackent.entities.AttachementType;
import com.luslusdawmpfe.PFEBackent.entities.Experience;
import com.luslusdawmpfe.PFEBackent.exceptions.EntityNotFoundException;
import com.luslusdawmpfe.PFEBackent.exceptions.IllegalFileEextensionException;
import com.luslusdawmpfe.PFEBackent.mappers.AttachementMapper;
import com.luslusdawmpfe.PFEBackent.mappers.ExperienceMapper;
import com.luslusdawmpfe.PFEBackent.repos.AppUserRepo;
import com.luslusdawmpfe.PFEBackent.repos.AttachementRepo;
import com.luslusdawmpfe.PFEBackent.repos.ExperienceRepo;
import com.luslusdawmpfe.PFEBackent.services.ExperienceService;
import com.luslusdawmpfe.PFEBackent.services.StorageService;
import com.luslusdawmpfe.PFEBackent.utils.FileUploadHelpers;
import com.luslusdawmpfe.PFEBackent.utils.SecurityCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExperienceServiceImpl implements ExperienceService {
    private final AppUserRepo appUserRepo;
    private final ExperienceRepo experienceRepo;
    private final ExperienceMapper experienceMapper;
    private final AttachementMapper attachementMapper;
    private final AttachementRepo attachementRepo;
    private final StorageService storageService;
    @Autowired
    private FileUploadHelpers fileUploadHelpers;

    @Override
    public ExperienceDto shareExperience(MultipartFile[] files, AddExperienceDto experience, @AuthenticationPrincipal AppUser user) {
        Set<String> imageExtentions = Set.of(".jpg",".png",".jpeg");
        Set<String> videoExtentions = Set.of(".mp4");
        var exp = experienceMapper.experienceDtoToExperience(experience);

        var attachements = Arrays.stream(files)
                        .map(attachement->{
                            var extension = fileUploadHelpers.getExtension(Objects.requireNonNull(attachement.getOriginalFilename()));
                            Optional<ApiResponseDto> result = Optional.empty();
                            try {
                                if(imageExtentions.contains(extension))
                                    result = Optional.ofNullable(storageService.uploadImage(attachement));
                                else if(videoExtentions.contains(extension))
                                    result = Optional.ofNullable(storageService.uploadVideo(attachement));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            return result.map(ApiResponseDto::getResponse)
                                    .map(att -> Attachement.builder()
                                            .experience(exp)
                                            .attachmentName((String) att.get("filename"))
                                            .attachmentUrl( (String) att.get("url"))
                                            .type((AttachementType) att.get("type"))
                                            .build())
                                    .orElseThrow();
                        }).collect(Collectors.toList());


        exp.setAuthor(user);
        exp.setAttachments(attachements);
       var savedExperience =  experienceRepo.save(exp);


        return experienceMapper.mapToExperienceDto(savedExperience);
    }

    @Override
    public ResponseEntity<List<ExperienceDto>> listExperiences(int pageNumber, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(
                pageNumber, pageSize, Sort.by(sortBy).descending());

        var experienceList = experienceRepo.findAll(paging).getContent().stream().map(
                experienceMapper::mapToExperienceDto).collect(Collectors.toList());
        return ResponseEntity.ok(experienceList);

    }

    @Override
    public ExperienceDto getOneExperience(Long experienceId) throws EntityNotFoundException {
        return experienceRepo.findById(experienceId)
                .map(experienceMapper::mapToExperienceDto)
                .orElseThrow(()->new EntityNotFoundException("No such experience found..."));

    }

    @Override
    public String updateExperience(Experience experience) throws EntityNotFoundException {
        var x = experienceRepo.findById(experience.getId())
        .orElseThrow(()->new EntityNotFoundException("Update Failed... No such exception!"));

        if(!SecurityCheck.isAdmin() || !SecurityCheck.isOwner(x.getAuthor().getUsername())) throw new AccessDeniedException("You are neither the admin or owner of this resource");
        experienceRepo.save(experience);
        return "Update completed successfully";
    }

    @Override
    public String deleteExperience(Long experienceId) throws EntityNotFoundException {
        var x = experienceRepo.findById(experienceId).orElseThrow(()->new EntityNotFoundException("Update Failed... No such exception!"));
        if(!SecurityCheck.isAdmin() || !SecurityCheck.isOwner(x.getAuthor().getUsername())) throw new AccessDeniedException("You are neither the admin or owner of this resource");
        var attachmentNames = x.getAttachments().stream()
                        .map(Attachement::getAttachmentName).collect(Collectors.toList());

        attachmentNames.forEach(an -> {
            try {
                storageService.deleteResource(an);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });


        experienceRepo.delete(x);
        return "Successfully deleted Experience";
    }

    @Override
    public ResponseEntity<List<ExperienceDto>> listPopularExperiences() {
        var experienceList = experienceRepo.findPopular();
       var popularExp= experienceList.stream().map(
                experienceMapper::mapToExperienceDto).collect(Collectors.toList());
        return ResponseEntity.ok(popularExp);

//        Pageable paging = PageRequest.of(
//                pageNumber, pageSize, Sort.by(sortBy).descending());
//
//        var experienceList = experienceRepo.findAll(paging).getContent().stream().map(
//                experienceMapper::mapToExperienceDto).collect(Collectors.toList());
//        return ResponseEntity.ok(experienceList);
    }

    @Override
    public ResponseEntity<List<ExperienceDto>> listLatestExperiences() {
        return null;
    }
}
