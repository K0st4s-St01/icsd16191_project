package com.icsd16191.project.mappers;

import com.icsd16191.project.models.dtos.PerformanceReviewDto;
import com.icsd16191.project.models.entities.PerformanceReview;
import com.icsd16191.project.models.entities.Performance;
import org.springframework.stereotype.Component;

@Component
public class PerformanceReviewMapper {
    public PerformanceReview toEntity(PerformanceReviewDto dto, Performance performance){
        return PerformanceReview
                .builder()
                .id(dto.getId() != null ? dto.getId() : null)
                .score(dto.getScore())
                .comments(dto.getComments())
                .performance(performance)
                .build();
    }
    public PerformanceReviewDto toDto(PerformanceReview entity,Long performance){
        return PerformanceReviewDto
                .builder()
                .id(entity.getId())
                .score(entity.getScore())
                .comments(entity.getComments())
                .performance(performance)
                .build();
    }
}
