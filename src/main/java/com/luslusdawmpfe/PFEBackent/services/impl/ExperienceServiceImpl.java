package com.luslusdawmpfe.PFEBackent.services.impl;
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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExperienceServiceImpl implements ExperienceService {
    private final AppUserRepo appUserRepo;
    private final ExperienceRepo experienceRepo;
    private final ExperienceMapper experienceMapper;
//    private final AttachementMapper attachementMapper;
//    private final AttachementRepo attachementRepo;

    @Override
    public String shareExperience(ExperienceDto experience, @AuthenticationPrincipal AppUser user) {
        //save the attachements
//        experience.getAttachments().forEach((attachment)->attachementRepo.save(attachementMapper.attachementDtoToAttachement(attachment)));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var exp = experienceMapper.experienceDtoToExperience(experience);
        exp.setAuthor(user);
        var c = experienceRepo.save(exp);
        return "Experienced submitted succefully: "+c;
    }

    @Override
    public Page<Experience> listExperiences(int pageNumber, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(
                pageNumber, pageSize, Sort.by(sortBy).descending());
        return experienceRepo.findAll(paging);

    }

    @Override
    public Experience getOneExperience(Long experienceId) throws EntityNotFoundException {
        return experienceRepo.findById(experienceId).orElseThrow(()->new EntityNotFoundException("No such experience found..."));
    }

    @Override
    public String updateExperience(Experience experience) throws EntityNotFoundException {
        experienceRepo.findById(experience.getId()).map(
                (x)->experienceRepo.save(experience)
        ).orElseThrow(()->new EntityNotFoundException("Update Failed... No such exception!"));
        return "Update completed successfully";
    }

    @Override
    public String deleteExperience(Experience experience) throws EntityNotFoundException {
        var x = experienceRepo.findById(experience.getId()).orElseThrow(()->new EntityNotFoundException("Update Failed... No such exception!"));
        experienceRepo.delete(x);
        return "Successfully deleted Experience";
    }

    //Take this to a utility class
//    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//    public Boolean isAdmin(AppUser appUser){
//        return auth.getAuthorities().contains(new SimpleGrantedAuthority("APP_ADMIN"));
//    }
//    public Boolean isExperienceOwner(Experience experience){
//        return experience.getAuthor().getUsername().equalsIgnoreCase(auth.getPrincipal().toString());
//    }
}
