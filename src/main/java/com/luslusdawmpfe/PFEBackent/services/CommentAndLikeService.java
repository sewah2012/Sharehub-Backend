package com.luslusdawmpfe.PFEBackent.services;

import com.luslusdawmpfe.PFEBackent.dtos.CommentDto;
import com.luslusdawmpfe.PFEBackent.entities.Comment;

import java.util.List;

public interface CommentAndLikeService {
    String addComment(CommentDto comment, Long experienceId);
    String deleteComment(Long commentId,Long experienceId);
    List<Comment> getComments(Long experienceId);

    //Likes and Unlike ...
    String likeAndUnlikeExperience(Long experienceId, String userName);

}
