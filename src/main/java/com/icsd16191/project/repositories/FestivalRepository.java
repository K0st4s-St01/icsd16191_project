package com.icsd16191.project.repositories;

import com.icsd16191.project.models.entities.Festival;
import com.icsd16191.project.models.entities.Performance;
import com.icsd16191.project.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Repository
public interface FestivalRepository extends JpaRepository<Festival,Long> {
    boolean existsByName(String name);

    List<Festival> findByNameIgnoreCase(String name);

    List<Festival> findByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCase(String name, String description);
}
