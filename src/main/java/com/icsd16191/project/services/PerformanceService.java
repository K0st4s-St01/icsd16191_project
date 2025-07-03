package com.icsd16191.project.services;

import com.icsd16191.project.mappers.PerformanceMapper;
import com.icsd16191.project.mappers.PerformanceReviewMapper;
import com.icsd16191.project.models.dtos.PerformanceDto;
import com.icsd16191.project.models.dtos.PerformanceReviewDto;
import com.icsd16191.project.models.entities.*;
import com.icsd16191.project.repositories.*;
import com.icsd16191.project.utils.FestivalDateFormatter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Service
@AllArgsConstructor
public class PerformanceService {
    private PerformanceMapper performanceMapper;
    private PerformanceRepository performanceRepository;
    private UserRepository userRepository;
    private MerchandiseRepository merchandiseRepository;
    private FestivalRepository festivalRepository;
    private PerformanceReviewMapper performanceReviewMapper;
    private PerformanceReviewRepository performanceReviewRepository;

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
        var entity = performanceMapper.toEntity(
                dto
                ,userRepository.findAllById(dto.getBandMembers())
                ,null
                ,new Date()
                ,dto.getMerchandiseItems()!=null && !dto.getMerchandiseItems().isEmpty() ? merchandiseRepository.findAllById(dto.getMerchandiseItems()) : null
                ,rehearsalDates
                ,timeSlots
                , PerformanceState.CREATED
                ,festivalRepository.findById(dto.getFestival()).orElseThrow()
                ,null
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
            var entity = performanceMapper.toEntity(
                    dto
                    , userRepository.findAllById(dto.getBandMembers())
                    , null
                    , previous.getCreationDate()
                    , dto.getMerchandiseItems() != null && !dto.getMerchandiseItems().isEmpty() ? merchandiseRepository.findAllById(dto.getMerchandiseItems()) : null
                    , rehearsalDates
                    , timeSlots
                    , dto.getPerformanceState()!= null ? PerformanceState.valueOf(dto.getPerformanceState()) : PerformanceState.CREATED
                    ,festivalRepository.findById(dto.getId()).orElseThrow()
                    ,null
            );

            performanceRepository.save(entity);
            return Map.of("result", "successfully updated", "dto", dto);
        }
        throw new Exception("performance does not exist");
    }
    public Map<String,Object> bandMemberAddition(String bandMember,Long performanceId) throws Exception {
        if(bandMember == null || performanceId == null){
            throw new Exception("bandMember or id null");
        }
        if (performanceRepository.existsById(performanceId) && userRepository.existsById(bandMember)){
            var performance = performanceRepository.findById(performanceId).orElseThrow();
            var user = userRepository.findById(bandMember).orElseThrow();
            if (!user.getRoles().contains("ARTIST")){
                throw new Exception("user "+user.getUsername()+" is not an artist");
            }
            performance.getBandMembers().add(user);

            performanceRepository.save(performance);

            return Map.of("result","band member "+bandMember+" successfully added to performance "+performance.getName()+"-"+performanceId);

        }

        throw new Exception("performance or user does not exist");
    }
    public Map<String,Object> performanceSubmission(Long performanceId) throws Exception {
        var performanceToBeSubmitted=performanceRepository.findById(performanceId).orElseThrow();
        //validation
        if (
                performanceToBeSubmitted.getName() != null
                    && performanceToBeSubmitted.getDescription() !=null
                    && performanceToBeSubmitted.getGenre() !=null
                    && performanceToBeSubmitted.getDuration()!=null
                    && performanceToBeSubmitted.getBandMembers()!=null && !performanceToBeSubmitted.getBandMembers().isEmpty()
                    && performanceToBeSubmitted.getSetList() != null
                    && performanceToBeSubmitted.getMerchandiseItems() !=null && !performanceToBeSubmitted.getMerchandiseItems().isEmpty()
                    && performanceToBeSubmitted.getPreferredPerformanceTimeSlots() !=null && !performanceToBeSubmitted.getPreferredPerformanceTimeSlots().isEmpty()
                    && performanceToBeSubmitted.getPreferredRehearsalTimes() !=null && !performanceToBeSubmitted.getPreferredRehearsalTimes().isEmpty()

        ){
            performanceToBeSubmitted.setPerformanceState(PerformanceState.SUBMITTED);
            performanceRepository.save(performanceToBeSubmitted);
            return Map.of("result","performance "+performanceToBeSubmitted.getName()+"-"+performanceId+" successfully submitted");
        }
        throw new Exception("data incomplete");
    }
    public Map<String,Object> performanceWithdrawal(Long id) throws Exception {
        var performanceToBeSubmitted=performanceRepository.findById(id).orElseThrow();
        if (performanceToBeSubmitted.getPerformanceState().equals(PerformanceState.CREATED)){
            performanceRepository.deleteById(id);
            return Map.of("result","performance "+performanceToBeSubmitted.getName()+"-"+id+" successfully withdrawn");
        }else{
            throw new Exception("performance has reached SUBMITTED state and cannot be withdrawn");
        }
    }
    public Map<String,Object> staffAssignment(String staffMember,Long performanceId) throws Exception {
        var staffUser = userRepository.findById(staffMember).orElseThrow();
        if(staffUser.getRoles().contains("STAFF")){
            var performance = performanceRepository.findById(performanceId).orElseThrow();
            performance.setStaff(staffUser);
            performanceRepository.save(performance);
            return Map.of("result","performance "+performance.getName()+"-"+performanceId+" successfully assigned "+staffMember+" as corresponding staff member for performance");
        }else{
            throw new Exception(staffMember+" not staff member");
        }
    }
    public Map<String,Object> performanceReview(String staff , PerformanceReviewDto dto,Long performance) throws Exception {
        var performanceObj = performanceRepository.findById(performance).orElseThrow();
        if(!performanceObj.getFestival().getFestivalState().equals(FestivalState.SCHEDULING)){
            throw new Exception("Festival not in SCHEDULING state");
        }
        var user = userRepository.findById(staff).orElseThrow();
        if(!user.getRoles().contains("STAFF") || !performanceObj.getStaff().getUsername().equals(staff)){
            throw new Exception("wrong staff user");
        }
        var review = performanceReviewMapper.toEntity(dto,performanceObj);
        performanceReviewRepository.save(review);
        performanceObj.setReview(review);
        performanceObj.setPerformanceState(PerformanceState.REVIEWED);
        performanceRepository.save(performanceObj);
        return Map.of("result","successful","dto",dto);
    }
    public Map<String,Object> performanceRejection(Long perfomanceId, PerformanceReviewDto rejectionReasons) throws Exception {
        var performance = performanceRepository.findById(perfomanceId).orElseThrow();
        performance.setPerformanceState(PerformanceState.REJECTED);
        switch (performance.getFestival().getFestivalState()){
            case SCHEDULING, DECISION -> {
                if (rejectionReasons == null){
                    var review = PerformanceReview.builder()
                            .id(null)
                            .score(0)
                            .comments("Failed to submit final performance details")
                            .build();
                    performance.setReview(review);
                    performanceReviewRepository.save(review);
                    performanceRepository.save(performance);
                    return Map.of("result","performance "+performance.getName()+""+perfomanceId+" successfully rejected");
                }
                var review = performanceReviewMapper.toEntity(rejectionReasons,performance);
                performance.setReview(review);
                performanceReviewRepository.save(review);
                performanceRepository.save(performance);

                return Map.of("result","performance "+performance.getName()+""+perfomanceId+" successfully rejected");
            }
            default -> throw new Exception("Unsupported festival state");
        }

    }
    public Map<String,Object> performanceFinalSubmission(Long performanceId,PerformanceDto dto) throws Exception {
        var result = this.performanceUpdate(dto,performanceId);
        performanceRepository.updatePerformanceStateById(PerformanceState.FINAL_SUBMISSION,performanceId);
        result.put("result","final submission complete");
        return result;
    }


}
