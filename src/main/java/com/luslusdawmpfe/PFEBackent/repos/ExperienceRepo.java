package com.luslusdawmpfe.PFEBackent.repos;

import com.luslusdawmpfe.PFEBackent.entities.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExperienceRepo extends JpaRepository<Experience, UUID> {
    @Query(value = "select(*) from experience ex where count(ex.comments) > 5 AND count(ex.likes) > 10 order by count(ex.likes) desc",nativeQuery=true)
    List<Experience> findPopular();

    @Query(value = "SELECT e FROM Experience e WHERE "
        +"CONCAT(lower(e.title), lower(e.details))"
            +"LIKE CONCAT('%',lower(?1),'%')"
    )
    List<Experience> findMatching(String term);

}
