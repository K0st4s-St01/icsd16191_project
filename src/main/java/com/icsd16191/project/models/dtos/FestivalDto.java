package com.icsd16191.project.models.dtos;

import com.icsd16191.project.models.entities.FestivalState;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FestivalDto {
    private Long id;
    private String name;
    private String description;
    private List<String> dates = new ArrayList<>();

    private List<String> organizers;

    private List<String> staff = new ArrayList<>();

    private String festivalState;

    private List<Long> performances;
}
