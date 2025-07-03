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
public class Performance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date creationDate;
    private String name;
    private String description;
    private String genre;
    private Long duration;
    @ManyToMany
    @JoinTable(
            name = "band_members",
            joinColumns = @JoinColumn(name = "bandMember"),
            inverseJoinColumns = @JoinColumn(name = "performance_id")
    )
    private List<User> bandMembers = new ArrayList<>();

    private String technicalRequirements;
    @Column(columnDefinition = "TEXT")
    private String setList;

    @OneToMany(mappedBy = "performance")
    private List<MerchandiseItem> merchandiseItems;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Date> preferredRehearsalTimes = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Date> preferredPerformanceTimeSlots = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private PerformanceState performanceState;

    @OneToOne
    @JoinColumn
    private User staff;

    @ManyToOne
    @JoinColumn
    private Festival festival;

    @OneToOne
    @JoinColumn
    private PerformanceReview review;

}
