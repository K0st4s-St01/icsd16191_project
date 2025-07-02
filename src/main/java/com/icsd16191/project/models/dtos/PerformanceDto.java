package com.icsd16191.project.models.dtos;

import com.icsd16191.project.models.entities.PerformanceState;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PerformanceDto {
    private Long id;
    private String name;
    private String description;
    private String genre;
    private Long duration;
    private List<String> bandMembers = new ArrayList<>();

    private String technicalRequirements;
    private String setList;

    private List<Long> merchandiseItems;

    private List<String> preferredRehearsalTimes = new ArrayList<>();

    private List<String> preferredPerformanceTimeSlots = new ArrayList<>();

    private String performanceState;

    private String staff;

    private Long festival;
}
