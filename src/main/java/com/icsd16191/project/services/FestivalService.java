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
        var roles = userRepository.findById(creator).orElseThrow().getRoles();
        System.out.println(roles);
        if (roles.contains("ROLE_ORGANIZER")){
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
    public Map<String,Object> festivalView(Long id){
        var entity = festivalRepository.findById(id).orElseThrow();
        return Map.of("result","successful","dto"
                , festivalMapper.toDto(
                        entity,
                        entity.getOrganizers().stream().map(User::getUsername).collect(Collectors.toList()),
                        entity.getStaff().stream().map(User::getUsername).collect(Collectors.toList()),
                        entity.getDates().stream().map(FestivalDateFormatter::turnToString).collect(Collectors.toList()),
                        entity.getPerformances().stream().map(Performance::getId).collect(Collectors.toList())
                ));
    }
    public Map<String,Object> festivalDelete(Long id,String user) throws Exception {
        var entity = festivalRepository.findById(id).orElseThrow();
        if (entity.getOrganizers().stream().anyMatch(user1 -> {
            return user1.getUsername().equals(user);
        })){
            if (entity.getFestivalState().equals(FestivalState.CREATED)) {
                festivalRepository.deleteById(id);
                return Map.of("result","successful");
            }else{
                throw new Exception("Festival not in correct state");
            }
        }else{
            throw new Exception("wrong user");
        }
    }
    public Map<String,Object> submissionStart(Long id,String user) throws Exception {
        var entity = festivalRepository.findById(id).orElseThrow();
        if (entity.getOrganizers().stream().anyMatch(user1 -> {
            return user1.getUsername().equals(user);
        })){
            if (entity.getFestivalState().equals(FestivalState.CREATED)) {
                festivalRepository.updateFestivalStateById(FestivalState.SUBMISSION,id);
                return Map.of("result","successful");
            }else{
                throw new Exception("Festival not in correct state");
            }
        }else{
            throw new Exception("wrong user");
        }
    }
    public Map<String,Object> assignmentStart(Long id,String user) throws Exception {
        var entity = festivalRepository.findById(id).orElseThrow();
        if (entity.getOrganizers().stream().anyMatch(user1 -> {
            return user1.getUsername().equals(user);
        })){
            if (entity.getFestivalState().equals(FestivalState.SUBMISSION)) {
                festivalRepository.updateFestivalStateById(FestivalState.ASSIGNMENT,id);
                return Map.of("result","successful");
            }else{
                throw new Exception("Festival not in correct state");
            }
        }else{
            throw new Exception("wrong user");
        }
    }
    public Map<String,Object> reviewStart(Long id,String user) throws Exception {
        var entity = festivalRepository.findById(id).orElseThrow();
        if (entity.getOrganizers().stream().anyMatch(user1 -> {
            return user1.getUsername().equals(user);
        })){
            if (entity.getFestivalState().equals(FestivalState.ASSIGNMENT)) {
                festivalRepository.updateFestivalStateById(FestivalState.REVIEW,id);
                return Map.of("result","successful");
            }else{
                throw new Exception("Festival not in correct state");
            }
        }else{
            throw new Exception("wrong user");
        }
    }
    public Map<String,Object> schedulingState(Long id,String user) throws Exception {
        var entity = festivalRepository.findById(id).orElseThrow();
        if (entity.getOrganizers().stream().anyMatch(user1 -> {
            return user1.getUsername().equals(user);
        })){
            if (entity.getFestivalState().equals(FestivalState.REVIEW)) {
                festivalRepository.updateFestivalStateById(FestivalState.SCHEDULING,id);
                return Map.of("result","successful");
            }else{
                throw new Exception("Festival not in correct state");
            }
        }else{
            throw new Exception("wrong user");
        }
    }
    public Map<String,Object> finalSubmissionState(Long id,String user) throws Exception {
        var entity = festivalRepository.findById(id).orElseThrow();
        if (entity.getOrganizers().stream().anyMatch(user1 -> {
            return user1.getUsername().equals(user);
        })){
            if (entity.getFestivalState().equals(FestivalState.SCHEDULING)) {
                festivalRepository.updateFestivalStateById(FestivalState.FINAL_SUBMISSION,id);
                return Map.of("result","successful");
            }else{
                throw new Exception("Festival not in correct state");
            }
        }else{
            throw new Exception("wrong user");
        }
    }
    public Map<String,Object> decisionState(Long id,String user) throws Exception {
        var entity = festivalRepository.findById(id).orElseThrow();
        if (entity.getOrganizers().stream().anyMatch(user1 -> {
            return user1.getUsername().equals(user);
        })){
            if (entity.getFestivalState().equals(FestivalState.FINAL_SUBMISSION)) {
                festivalRepository.updateFestivalStateById(FestivalState.DECISION,id);
                return Map.of("result","successful");
            }else{
                throw new Exception("Festival not in correct state");
            }
        }else{
            throw new Exception("wrong user");
        }
    }
    public Map<String,Object> announcementState(Long id,String user) throws Exception {
        var entity = festivalRepository.findById(id).orElseThrow();
        if (entity.getOrganizers().stream().anyMatch(user1 -> {
            return user1.getUsername().equals(user);
        })){
            if (entity.getFestivalState().equals(FestivalState.DECISION)) {
                festivalRepository.updateFestivalStateById(FestivalState.ANNOUNCED,id);
                return Map.of("result","successful");
            }else{
                throw new Exception("Festival not in correct state");
            }
        }else{
            throw new Exception("wrong user");
        }
    }
}
