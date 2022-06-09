package com.luslusdawmpfe.PFEBackent.repos;

import com.luslusdawmpfe.PFEBackent.entities.Attachement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepo extends JpaRepository<Attachement, Long> {
}
