package com.icsd16191.project.mappers;

import com.icsd16191.project.models.dtos.PerformanceDto;
import com.icsd16191.project.models.entities.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class PerformanceMapper {
    public Performance toEntity(PerformanceDto dto
            , List<User> bandMembers
            , User staff
            , Date creationDate
            , List<MerchandiseItem> merchandiseItems
            , List<Date> preferredTimeSlots
            , List<Date> preferredRehearsalTimes
            , PerformanceState state
            , Festival festival
            ,PerformanceReview review
                                ){
        return Performance
                .builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .id(dto.getId() != null ? dto.getId() : null)
                .genre(dto.getGenre())
                .bandMembers(bandMembers)
                .staff(staff)
                .creationDate(creationDate)
                .merchandiseItems(merchandiseItems)
                .technicalRequirements(dto.getTechnicalRequirements())
                .preferredPerformanceTimeSlots(preferredTimeSlots)
                .preferredRehearsalTimes(preferredRehearsalTimes)
                .duration(dto.getDuration())
                .setList(dto.getSetList())
                .performanceState(state)
                .festival(festival)
                .performanceReview(review)
                .build();
    }
    public PerformanceDto toDto(
            Performance entity
            ,PerformanceState state
            ,List<String> bandMembers
            ,String staff
            ,List<Long> merchandise
            ,List<String> rehearsalDates
            ,List<String> timeSlots
            ,Long performanceReview
    ){
        return PerformanceDto
                .builder()
                .name(entity.getName())
                .description(entity.getDescription())
                .id(entity.getId())
                .bandMembers(bandMembers)
                .staff(staff)
                .duration(entity.getDuration())
                .genre(entity.getGenre())
                .setList(entity.getSetList())
                .merchandiseItems(merchandise)
                .performanceState(state.toString())
                .technicalRequirements(entity.getTechnicalRequirements())
                .preferredPerformanceTimeSlots(timeSlots)
                .preferredRehearsalTimes(rehearsalDates)
                .festival(entity.getFestival()!=null ? entity.getFestival().getId() : null)
                .perfomanceReview(performanceReview)
                .build();
    }
}
