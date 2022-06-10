package com.luslusdawmpfe.PFEBackent.services.impl;

import com.luslusdawmpfe.PFEBackent.dtos.CommentDto;
import com.luslusdawmpfe.PFEBackent.entities.AddCommentDto;
import com.luslusdawmpfe.PFEBackent.entities.AppUser;
import com.luslusdawmpfe.PFEBackent.entities.Comment;
import com.luslusdawmpfe.PFEBackent.exceptions.EntityNotFoundException;
import com.luslusdawmpfe.PFEBackent.mappers.CommentMapper;
import com.luslusdawmpfe.PFEBackent.repos.CommentRepo;
import com.luslusdawmpfe.PFEBackent.repos.ExperienceRepo;
import com.luslusdawmpfe.PFEBackent.services.CommentAndLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentAndLikeServiceImpl implements CommentAndLikeService {
    private final CommentRepo commentRepo;
    private final ExperienceRepo experienceRepo;
    private final CommentMapper commentMapper;

    @Override
    public String addComment(AddCommentDto comment, AppUser user) {
        comment.setAuthor(user);
        var x = commentMapper.AddCommentDtoToComment(comment);//        x.setAuthor(user);
         commentRepo.save(x);

         return "Comment successfully added";
    }

    @Override
    public String deleteComment(Long commentId) throws EntityNotFoundException {
        var comment = commentRepo.findById(commentId)
                .orElseThrow(()->new EntityNotFoundException("Comment Doest not exist!"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var authorities = authentication.getAuthorities();
        var loggedInuser = authentication.getName();
        if(authorities.contains(new SimpleGrantedAuthority("APP_ADMIN")) || comment.getAuthor().getUsername().equals(loggedInuser)) {
            commentRepo.findById(commentId).ifPresent(commentRepo::delete);
        } else {
            throw new AccessDeniedException("You are neither the admin or owner of this resource");
        }
        return "Comment Successfully deleted";
    }

    @Override
    public List<Comment> getComments(Long experienceId) {
        return null;
    }

    @Override
    public String likeAndUnlikeExperience(Long experienceId, String userName) throws EntityNotFoundException {
        var x  = experienceRepo.findById(experienceId)
                .map(exp->{
                    var likes = exp.getLikes();
                    var b = likes.contains(userName) ? likes.remove(userName) : likes.add(userName);
                    experienceRepo.save(exp);
                    return b;
                })
                .orElseThrow(()->new EntityNotFoundException("No such entity!!"));

        if(x) return "Like removed";
        return "Like added";
    }
}
