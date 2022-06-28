package com.luslusdawmpfe.PFEBackent.services;

import com.luslusdawmpfe.PFEBackent.dtos.CommentDto;
import com.luslusdawmpfe.PFEBackent.entities.AddCommentDto;
import com.luslusdawmpfe.PFEBackent.entities.AppUser;
import com.luslusdawmpfe.PFEBackent.entities.Comment;
import com.luslusdawmpfe.PFEBackent.exceptions.EntityNotFoundException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

public interface CommentAndLikeService {
    CommentDto addComment(AddCommentDto comment, AppUser user);
    String deleteComment(String CommentId) throws EntityNotFoundException;
    List<Comment> getComments(String experienceId);

    //Likes and Unlike ...
    String likeAndUnlikeExperience(String experienceId, @AuthenticationPrincipal AppUser user) throws EntityNotFoundException;

}
