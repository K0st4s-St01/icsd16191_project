package com.icsd16191.project.services;

import com.icsd16191.project.mappers.PerfomanceMapper;
import com.icsd16191.project.models.dtos.PerformanceDto;
import com.icsd16191.project.models.entities.Performance;
import com.icsd16191.project.models.entities.PerformanceState;
import com.icsd16191.project.repositories.MerchandiseRepository;
import com.icsd16191.project.repositories.PerformanceRepository;
import com.icsd16191.project.repositories.UserRepository;
import com.icsd16191.project.utils.FestivalDateFormatter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class FestivalService {
    private PerfomanceMapper perfomanceMapper;
    private PerformanceRepository performanceRepository;
    private UserRepository userRepository;
    private MerchandiseRepository merchandiseRepository;

    public Map<String,Object> performanceCreation(PerformanceDto dto){
        List<Date> rehearsalDates=new ArrayList<>();
        List<Date> timeSlots=new ArrayList<>();
        if(dto.getPreferredRehearsalTimes() != null && !dto.getPreferredRehearsalTimes().isEmpty()){
            for (String date : dto.getPreferredRehearsalTimes()){
                try {
                    rehearsalDates.add(FestivalDateFormatter.toDate(date));
                } catch (ParseException e) {
                    return Map.of("result","date format = dd.MM.yyyy->hh:mm","exception",e.getMessage());
                }
            }
        }
        if(dto.getPreferredPerformanceTimeSlots() != null && !dto.getPreferredRehearsalTimes().isEmpty()){
            for (String date : dto.getPreferredPerformanceTimeSlots()){
                try {
                    timeSlots.add(FestivalDateFormatter.toDate(date));
                } catch (ParseException e) {
                    return Map.of("result","date format = dd.MM.yyyy->hh:mm","exception",e.getMessage());
                }
            }
        }
        if(dto.getName() == null || dto.getDescription() == null || dto.getGenre() ==null || dto.getBandMembers() == null || dto.getDuration() == null){
            return Map.of("result","missing required data {name,description,genre,duration,bandmembers} are required");
        }
        if (performanceRepository.existsByName(dto.getName())){
            return Map.of("result","name must be unique , name "+dto.getName()+" already exists");
        }
        if(dto.getBandMembers().isEmpty()){
            return Map.of("result","band member data missing");
        }
        var entity = perfomanceMapper.toEntity(
                dto
                ,userRepository.findAllById(dto.getBandMembers())
                ,null
                ,new Date()
                ,dto.getMerchandiseItems()!=null && !dto.getMerchandiseItems().isEmpty() ? merchandiseRepository.findAllById(dto.getMerchandiseItems()) : null
                ,rehearsalDates
                ,timeSlots
                ,PerformanceState.CREATED
                );

        performanceRepository.save(entity);
        return Map.of("result","successful","dto",dto);
    }
    public Map<String,Object> performanceUpdate(PerformanceDto dto,Long id) throws Exception {

        List<Date> rehearsalDates=new ArrayList<>();
        List<Date> timeSlots=new ArrayList<>();
        if(dto.getPreferredRehearsalTimes() != null && !dto.getPreferredRehearsalTimes().isEmpty()){
            for (String date : dto.getPreferredRehearsalTimes()){
                try {
                    rehearsalDates.add(FestivalDateFormatter.toDate(date));
                } catch (ParseException e) {
                    return Map.of("result","date format = dd.MM.yyyy->hh:mm","exception",e.getMessage());
                }
            }
        }
        if(dto.getPreferredPerformanceTimeSlots() != null && !dto.getPreferredRehearsalTimes().isEmpty()){
            for (String date : dto.getPreferredPerformanceTimeSlots()){
                try {
                    timeSlots.add(FestivalDateFormatter.toDate(date));
                } catch (ParseException e) {
                    return Map.of("result","date format = dd.MM.yyyy->hh:mm","exception",e.getMessage());
                }
            }
        }
        if(dto.getName() == null || dto.getDescription() == null || dto.getGenre() ==null || dto.getBandMembers() == null || dto.getDuration() == null){
            return Map.of("result","missing required data {name,description,genre,duration,bandmembers} are required");
        }
        if(dto.getBandMembers().isEmpty()){
            return Map.of("result","band member data missing");
        }
        if (performanceRepository.existsById(id)) {
            dto.setId(id);
            var previous = performanceRepository.findById(id).orElseThrow();
            if(!previous.getName().equals(dto.getName())){
                if (performanceRepository.existsByName(dto.getName())){
                    throw new Exception("new name already exists");
                }
            }
            var entity = perfomanceMapper.toEntity(
                    dto
                    , userRepository.findAllById(dto.getBandMembers())
                    , null
                    , previous.getCreationDate()
                    , dto.getMerchandiseItems() != null && !dto.getMerchandiseItems().isEmpty() ? merchandiseRepository.findAllById(dto.getMerchandiseItems()) : null
                    , rehearsalDates
                    , timeSlots
                    , PerformanceState.CREATED
            );

            performanceRepository.save(entity);
            return Map.of("result", "successfully updated", "dto", dto);
        }
        throw new Exception("performance does not exist");
    }
}
