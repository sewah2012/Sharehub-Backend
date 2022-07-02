package com.luslusdawmpfe.PFEBackent.repos;

import com.luslusdawmpfe.PFEBackent.entities.Attachement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AttachementRepo extends JpaRepository<Attachement, UUID> {
    Optional<Attachement> findByAttachmentName(String attachmentName);
}
