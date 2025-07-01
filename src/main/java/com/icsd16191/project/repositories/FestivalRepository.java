package com.icsd16191.project.repositories;

import com.icsd16191.project.models.entities.Festival;
import com.icsd16191.project.models.entities.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FestivalRepository extends JpaRepository<Festival,Long> {
}
