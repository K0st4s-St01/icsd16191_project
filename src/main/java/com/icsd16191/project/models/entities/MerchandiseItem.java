package com.icsd16191.project.models.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MerchandiseItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String ItemType;
    private String price;

    @ManyToOne
    @JoinColumn
    private Performance performance;


}
