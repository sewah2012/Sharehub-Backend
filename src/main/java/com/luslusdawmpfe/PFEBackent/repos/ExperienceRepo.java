package com.luslusdawmpfe.PFEBackent.repos;

import com.luslusdawmpfe.PFEBackent.entities.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ExperienceRepo extends JpaRepository<Experience, Long> {
    @Query(value = "select(*) from experience ex where count(ex.comments) > 5 AND count(ex.likes) > 10 order by count(ex.likes) desc",nativeQuery=true)

    List<Experience> findPopular();
}
