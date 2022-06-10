package com.luslusdawmpfe.PFEBackent.services.impl;
import com.luslusdawmpfe.PFEBackent.dtos.AddExperienceDto;
import com.luslusdawmpfe.PFEBackent.dtos.ExperienceDto;
import com.luslusdawmpfe.PFEBackent.entities.AppUser;
import com.luslusdawmpfe.PFEBackent.entities.Experience;
import com.luslusdawmpfe.PFEBackent.exceptions.EntityNotFoundException;
import com.luslusdawmpfe.PFEBackent.mappers.AttachementMapper;
import com.luslusdawmpfe.PFEBackent.mappers.ExperienceMapper;
import com.luslusdawmpfe.PFEBackent.repos.AppUserRepo;
import com.luslusdawmpfe.PFEBackent.repos.AttachementRepo;
import com.luslusdawmpfe.PFEBackent.repos.ExperienceRepo;
import com.luslusdawmpfe.PFEBackent.services.ExperienceService;
import com.luslusdawmpfe.PFEBackent.utils.SecurityCheck;
import lombok.RequiredArgsConstructor;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExperienceServiceImpl implements ExperienceService {
    private final AppUserRepo appUserRepo;
    private final ExperienceRepo experienceRepo;
    private final ExperienceMapper experienceMapper;
    private final AttachementMapper attachementMapper;
    private final AttachementRepo attachementRepo;

    @Override
    public String shareExperience(AddExperienceDto experience, @AuthenticationPrincipal AppUser user) {
        var exp = experienceMapper.experienceDtoToExperience(experience);


        var attachements = experience.getAttachments()
                .stream()
                .map((att)->{
                    var x = attachementMapper.attachementDtoToAttachement(att);
                     x.setExperience(exp);
                     return x;
                })
                .collect(Collectors.toList());

        exp.setAuthor(user);
        exp.setAttachments(attachements);
        experienceRepo.save(exp);


        return "Experienced submitted succefully: ";
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
        experienceRepo.delete(x);
        return "Successfully deleted Experience";
    }

}
