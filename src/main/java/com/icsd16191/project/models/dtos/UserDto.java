package com.icsd16191.project.models.dtos;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    @Id
    private String username;
    private String password;

    private List<String> roles;

    private List<Long> performances = new ArrayList<>();

    private Long staffPerformance;

    private List<Long> festivals = new ArrayList<>();

    private List<Long> staffFestivals = new ArrayList<>();
}
