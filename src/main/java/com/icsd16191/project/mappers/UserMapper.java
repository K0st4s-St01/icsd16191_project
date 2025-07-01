package com.icsd16191.project.mappers;

import com.icsd16191.project.models.dtos.UserDto;
import com.icsd16191.project.models.entities.Festival;
import com.icsd16191.project.models.entities.Performance;
import com.icsd16191.project.models.entities.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {
    public User toEntity(
            UserDto dto
            , List<String> roles
            ,List<Festival> festivals
            ,List<Festival> staffFestivals
            ,List<Performance> performances
            ,Performance staffPerformance
    ){
        return User
                .builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .roles(roles)
                .festivals(festivals)
                .performances(performances)
                .staffFestivals(staffFestivals)
                .staffperformance(staffPerformance)
                .build();
    }
    public UserDto toDto(
            User entity
            ,List<String> roles
            ,List<Long> festivals
            ,List<Long> staffFestivals
            ,List<Long> performances
            ,Long staffPerformance

    ){
        return UserDto
                .builder()
                .username(entity.getUsername())
                .password(entity.getPassword())
                .roles(roles)
                .festivals(festivals)
                .performances(performances)
                .staffFestivals(staffFestivals)
                .staffPerformance(staffPerformance)
                .build();
    }
}
