package com.luslusdawmpfe.PFEBackent.mappers;

import com.luslusdawmpfe.PFEBackent.dtos.CommentAuthorDto;
import com.luslusdawmpfe.PFEBackent.dtos.CommentDto;
import com.luslusdawmpfe.PFEBackent.entities.AddCommentDto;
import com.luslusdawmpfe.PFEBackent.entities.AppUser;
import com.luslusdawmpfe.PFEBackent.entities.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.TargetType;

@Mapper(config=MapperConfiguration.class)
public interface CommentMapper {

    CommentDto mapToCommentDto(Comment comment);
    CommentAuthorDto mapToCommentAuthorDto(AppUser user);
    Comment AddCommentDtoToComment(AddCommentDto comment);

}
