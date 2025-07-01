package com.icsd16191.project.mappers;

import com.icsd16191.project.models.dtos.MerchandiseItemDto;
import com.icsd16191.project.models.entities.MerchandiseItem;
import com.icsd16191.project.models.entities.Performance;
import com.icsd16191.project.models.entities.User;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class MerchandiseItemMapper {
    public MerchandiseItem toEntity(MerchandiseItemDto dto, Performance performance){
        return MerchandiseItem
                .builder()
                .id(dto.getId() != null ? dto.getId() : null)
                .name(dto.getName())
                .description(dto.getDescription())
                .ItemType(dto.getItemType())
                .price(dto.getPrice())
                .performance(performance)
                .build();
    }
    public MerchandiseItemDto toDto(MerchandiseItem entity,Long performance){
        return MerchandiseItemDto
                .builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .ItemType(entity.getItemType())
                .price(entity.getPrice())
                .performance(performance)
                .build();
    }
}
