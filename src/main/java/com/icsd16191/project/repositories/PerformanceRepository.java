package com.icsd16191.project.repositories;

import com.icsd16191.project.models.entities.Performance;
import com.icsd16191.project.models.entities.PerformanceState;
import com.icsd16191.project.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance,Long> {
    boolean existsByName(String name);

    @Transactional
    @Modifying
    @Query("update Performance p set p.performanceState = ?1 where p.id = ?2")
    void updatePerformanceStateById(PerformanceState performanceState, Long id);
}
