package com.luslusdawmpfe.PFEBackent.services;

import com.luslusdawmpfe.PFEBackent.dtos.CommentDto;
import com.luslusdawmpfe.PFEBackent.entities.AddCommentDto;
import com.luslusdawmpfe.PFEBackent.entities.AppUser;
import com.luslusdawmpfe.PFEBackent.entities.Comment;
import com.luslusdawmpfe.PFEBackent.exceptions.EntityNotFoundException;

import java.util.List;

public interface CommentAndLikeService {
    String addComment(AddCommentDto comment, AppUser user);
    String deleteComment(Long CommentId) throws EntityNotFoundException;
    List<Comment> getComments(Long experienceId);

    //Likes and Unlike ...
    String likeAndUnlikeExperience(Long experienceId, String userName) throws EntityNotFoundException;

}
