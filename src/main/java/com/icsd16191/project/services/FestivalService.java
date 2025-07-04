package com.icsd16191.project.services;

import com.icsd16191.project.mappers.FestivalMapper;
import com.icsd16191.project.mappers.UserMapper;
import com.icsd16191.project.models.dtos.FestivalDto;
import com.icsd16191.project.models.entities.FestivalState;
import com.icsd16191.project.models.entities.Performance;
import com.icsd16191.project.models.entities.User;
import com.icsd16191.project.repositories.FestivalRepository;
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
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FestivalService {
    private FestivalRepository festivalRepository;
    private FestivalMapper festivalMapper;
    private UserRepository userRepository;
    private PerformanceRepository performanceRepository;
    public Map<String,Object> festivalCreation(String creator,FestivalDto dto) throws Exception {
        if (festivalRepository.existsByName(dto.getName())){
            throw new Exception("festival "+dto.getName()+" already exists");
        }
        if (userRepository.findById(creator).orElseThrow().getRoles().contains("Organizer")){
            throw new Exception("user not organizer");
        }
        List<Date> dates = dto.getDates() != null && !dto.getDates().isEmpty() ? dto.getDates().stream().map(date -> {
            try {
                return FestivalDateFormatter.toDate(date);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList()) : new ArrayList<>();
        var festivalEntity=festivalMapper.toEntity(
                dto,
                FestivalState.CREATED,
                new ArrayList<>(List.of(userRepository.findById(creator).orElseThrow())),
                new ArrayList<User>(),
                new Date(),
                dates,
                dto.getPerformances()!=null?performanceRepository.findAllById(dto.getPerformances()):new ArrayList<>()

        );
        festivalRepository.save(festivalEntity);
        return Map.of("result","successful","dto",dto);
    }

    public Map<String,Object> festivalUpdate(String organizer,FestivalDto dto,Long festivalId) throws Exception {
        if (!festivalRepository.existsByName(dto.getName())){
            throw new Exception("festival "+dto.getName()+" already exists");
        }
        dto.setId(festivalId);
        var oldEntity = festivalRepository.findById(festivalId).orElseThrow();
        if (oldEntity.getOrganizers().stream().noneMatch(user-> user.getUsername().equals(organizer))){
            throw new Exception("user "+organizer+" cannot edit this festival");
        }
        List<Date> dates = dto.getDates() != null && !dto.getDates().isEmpty() ? dto.getDates().stream().map(date -> {
            try {
                return FestivalDateFormatter.toDate(date);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList()) : new ArrayList<>();
        var festivalEntity=festivalMapper.toEntity(
                dto,
                oldEntity.getFestivalState(),
                oldEntity.getOrganizers(),
                oldEntity.getStaff(),
                oldEntity.getCreationDate(),
                dates,
                dto.getPerformances()!=null?performanceRepository.findAllById(dto.getPerformances()):new ArrayList<>()

        );
        festivalRepository.save(festivalEntity);
        return Map.of("result","successful","dto",dto);
    }
    public Map<String,Object> organizersAddition(Long id,String organizer,List<String> organizersNew) throws Exception {
        var oldEntity = festivalRepository.findById(id).orElseThrow();
        if (oldEntity.getOrganizers().stream().noneMatch(user-> user.getUsername().equals(organizer))){
            throw new Exception("user "+organizer+" cannot edit this festival");
        }
        oldEntity.getOrganizers().addAll(userRepository.findAllById(organizersNew));
        festivalRepository.save(oldEntity);
        return Map.of("result","successful");

    }
    public Map<String,Object> staffAddition(Long id,String organizer,List<String> staffNew) throws Exception {
        var oldEntity = festivalRepository.findById(id).orElseThrow();
        if (oldEntity.getOrganizers().stream().noneMatch(user-> user.getUsername().equals(organizer))){
            throw new Exception("user "+organizer+" cannot edit this festival");
        }
        oldEntity.getStaff().addAll(userRepository.findAllById( staffNew));
        festivalRepository.save(oldEntity);
        return Map.of("result","successful");

    }
    /*
    The search criteria can include
the name of the festival, its description, dates, and venue. If more than one word is given for a
field/criterion, then all the words need to be included in this field for a festival to be matched.
If no word is given for any of the aforementioned criteria, then all the festivals are matched.
     */
    public Map<String,Object> festivalSearch(List<String> keywords){
        var dtos = new ArrayList<FestivalDto>();
        if (keywords.isEmpty()){
                for(var festEntity : festivalRepository.findAll()){
                    dtos.add(
                            festivalMapper.toDto(
                                    festEntity,
                                    festEntity.getOrganizers().stream().map(User::getUsername).collect(Collectors.toList()),
                                    festEntity.getStaff().stream().map(User::getUsername).collect(Collectors.toList()),
                                    festEntity.getDates().stream().map(FestivalDateFormatter::turnToString).collect(Collectors.toList()),
                                    festEntity.getPerformances().stream().map(Performance::getId).collect(Collectors.toList())
                                    )
                    );
                }
            return Map.of("result","successful","dtos",dtos);
        }
        for (String word : keywords){
            for(var festEntity : festivalRepository.findByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCase(word,word)){
                dtos.add(
                        festivalMapper.toDto(
                                festEntity,
                                festEntity.getOrganizers().stream().map(User::getUsername).collect(Collectors.toList()),
                                festEntity.getStaff().stream().map(User::getUsername).collect(Collectors.toList()),
                                festEntity.getDates().stream().map(FestivalDateFormatter::turnToString).collect(Collectors.toList()),
                                festEntity.getPerformances().stream().map(Performance::getId).collect(Collectors.toList())
                                )
                );
            }
        }
        return Map.of("result","successful","dtos",dtos);
    }
}
