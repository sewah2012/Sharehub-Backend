package com.luslusdawmpfe.PFEBackent.repos;

import com.luslusdawmpfe.PFEBackent.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommentRepo extends JpaRepository<Comment, UUID> {
}
