package com.icsd16191.project.repositories;

import com.icsd16191.project.models.entities.Festival;
import com.icsd16191.project.models.entities.PerformanceReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceReviewRepository extends JpaRepository<PerformanceReview,Long> {
}
