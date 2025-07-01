package com.icsd16191.project.models.dtos;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MerchandiseItemDto {
    private Long id;
    private String name;
    private String description;
    private String ItemType;
    private Float price;

    private Long performance;


}
