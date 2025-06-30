package com.icsd16191.project.repositories;

import com.icsd16191.project.models.entities.Performance;
import com.icsd16191.project.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance,Long> {
}
