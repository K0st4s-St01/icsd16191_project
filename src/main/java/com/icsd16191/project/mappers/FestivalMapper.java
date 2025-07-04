package com.icsd16191.project.mappers;

import com.icsd16191.project.models.dtos.FestivalDto;
import com.icsd16191.project.models.entities.Festival;
import com.icsd16191.project.models.entities.FestivalState;
import com.icsd16191.project.models.entities.Performance;
import com.icsd16191.project.models.entities.User;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class FestivalMapper {
    public Festival toEntity(FestivalDto dto, FestivalState state, List<User> organizers, List<User> staff, Date creationDate, List<Date> dates, List<Performance> performances){
        return Festival
                .builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .organizers(organizers)
                .id(dto.getId() != null ? dto.getId() : null)
                .staff(staff)
                .creationDate(creationDate)
                .festivalState(state)
                .dates(dates)
                .performances(performances)
                .build();
    }
    public FestivalDto toDto(Festival entity,List<String> organizers,List<String> staff,List<String> dates,List<Long> performances){
        return FestivalDto
                .builder()
                .name(entity.getName())
                .description(entity.getDescription())
                .id(entity.getId())
                .organizers(organizers)
                .staff(staff)
                .dates(dates)
                .festivalState(entity.getFestivalState().toString())
                .performances(performances)
                .build();
    }
}
