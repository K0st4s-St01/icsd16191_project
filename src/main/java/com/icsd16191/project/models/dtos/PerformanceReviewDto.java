package com.icsd16191.project.models.dtos;

import com.icsd16191.project.models.entities.Performance;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class PerformanceReviewDto {
    private Long id;
    private Integer score;
    private String comments;

    private Long performance;
}
/*

 The review must contain
both a numerical score and detailed written comments that reflect the performance’stechnical
and artistic evaluation. The review is accepted only if the festival is in the REVIEW state. This
approach ensures that the evaluation comes from someone with deep understanding of both
the technical requirements and the performance's overall fit with the festival.


 */