package com.luslusdawmpfe.PFEBackent.controllers;

import com.luslusdawmpfe.PFEBackent.dtos.CommentDto;
import com.luslusdawmpfe.PFEBackent.entities.AddCommentDto;
import com.luslusdawmpfe.PFEBackent.entities.AppUser;
import com.luslusdawmpfe.PFEBackent.entities.Comment;
import com.luslusdawmpfe.PFEBackent.exceptions.EntityNotFoundException;
import com.luslusdawmpfe.PFEBackent.services.CommentAndLikeService;
import com.luslusdawmpfe.PFEBackent.utils.AppAdminOrOwner;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reaction")
@RequiredArgsConstructor
public class ReactionsController {
    private final CommentAndLikeService service;

    @PreAuthorize("hasAnyAuthority({'APP_USER','APP_ADMIN'})")
    @PostMapping("/comment")
    ResponseEntity<String> comment (@RequestBody AddCommentDto comment, @AuthenticationPrincipal AppUser user){
        return ResponseEntity.ok(service.addComment(comment, user));
    }

//    @AppAdminOrOwner
    @PreAuthorize("hasAnyAuthority({'APP_USER','APP_ADMIN'})")
    @DeleteMapping("/comment/delete/{commentId}")
    ResponseEntity<String> deleteComment ( @PathVariable("commentId") Long comment) throws EntityNotFoundException {
        return ResponseEntity.ok(service.deleteComment(comment));
    }
}
