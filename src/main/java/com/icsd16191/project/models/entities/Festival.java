package com.icsd16191.project.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Festival {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date creationDate;
    private String name;
    private String description;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Date> dates = new ArrayList<>();

    @ManyToMany
    @JoinTable
    private List<User> organizers;

    @ManyToMany(mappedBy = "staffFestivals")
    private List<User> staff = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private FestivalState festivalState;

    @OneToMany(mappedBy = "festival")
    private List<Performance> performances = new ArrayList<>();
}
