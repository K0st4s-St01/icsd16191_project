package com.icsd16191.project.rest;

import com.icsd16191.project.mappers.PerformanceMapper;
import com.icsd16191.project.mappers.PerformanceReviewMapper;
import com.icsd16191.project.models.dtos.PerformanceDto;
import com.icsd16191.project.models.dtos.PerformanceReviewDto;
import com.icsd16191.project.models.entities.*;
import com.icsd16191.project.repositories.*;
import com.icsd16191.project.services.PerformanceService;
import com.icsd16191.project.utils.FestivalDateFormatter;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
@AllArgsConstructor
@RestController
@RequestMapping(path = "/performance")
public class PerformanceRestController {
    private PerformanceService service;
    @PostMapping
    public Map<String,Object> performanceCreation(@RequestBody PerformanceDto dto){
        try {
            if(!dto.getBandMembers().contains(SecurityContextHolder.getContext().getAuthentication().getName()))
                throw new Exception("wrong user");
            return service.performanceCreation(dto);
        }catch (Exception e){
            return  Map.of("result",e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public Map<String,Object> performanceUpdate(@RequestBody PerformanceDto dto,@PathVariable("id") Long id) throws Exception {
        try {
            if(!dto.getBandMembers().contains(SecurityContextHolder.getContext().getAuthentication().getName()))
                throw new Exception("wrong user");
            return service.performanceUpdate(dto,id);
        }catch (Exception e){
            return  Map.of("result",e.getMessage());
        }
    }
    @PostMapping("/{id}/member/{bandMember}")
    public Map<String,Object> bandMemberAddition(@PathVariable("bandMember") String bandMember,@PathVariable("id") Long performanceId) throws Exception {
        try {
            return service.bandMemberAddition(bandMember,performanceId,SecurityContextHolder.getContext().getAuthentication().getName());
        }catch (Exception e){
            return  Map.of("result",e.getMessage());
        }
    }
    @PostMapping("/{id}/submit/")
    public Map<String,Object> performanceSubmission(@PathVariable("id") Long performanceId) throws Exception {
        try {
            if (artistBelongsToPerfomance(performanceId,SecurityContextHolder.getContext().getAuthentication().getName()))
                throw new Exception("wrong user");
            return service.performanceSubmission(performanceId);
        }catch (Exception e){
            return Map.of("result",e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public Map<String,Object> performanceWithdrawal(@PathVariable("id") Long id) throws Exception {
        try {
            if (artistBelongsToPerfomance(id,SecurityContextHolder.getContext().getAuthentication().getName()))
                throw new Exception("wrong user");
            return service.performanceWithdrawal(id);
        }catch (Exception e){
            return Map.of("result",e.getMessage());
        }
    }
    @PostMapping("{id}/staff/{staff}")
    public Map<String,Object> staffAssignment(@PathVariable("staff") String staffMember,@PathVariable("id") Long performanceId) throws Exception {
        try{
            return service.staffAssignment(staffMember,performanceId);
        }catch (Exception e){
            return Map.of("result",e.getMessage());
        }
    }
    @PostMapping("{id}/staff/{staff}/review")
    public Map<String,Object> performanceReview(@PathVariable("staff") String staff ,@RequestBody PerformanceReviewDto dto,@PathVariable("id") Long performance) throws Exception {

        try{
           return service.performanceReview(staff,dto,performance);
        }catch (Exception e){
            return Map.of("result",e.getMessage());
        }
    }
    @DeleteMapping("{id}/staff/rejection")
    public Map<String,Object> performanceRejection(@PathVariable("id") Long perfomanceId,@RequestBody PerformanceReviewDto rejectionReasons) throws Exception {

        try{
           return service.performanceRejection(perfomanceId,rejectionReasons);
        }catch (Exception e){
            return Map.of("result",e.getMessage());
        }
    }
    @PostMapping("/{id}/final_submit")
    public Map<String,Object> performanceFinalSubmission(@PathVariable("{id}") Long performanceId,@RequestBody PerformanceDto dto) throws Exception {

        try{
           return service.performanceFinalSubmission(performanceId,dto);
        }catch (Exception e){
            return Map.of("result",e.getMessage());
        }
    }

    @PostMapping("{id}/staff/accept")
    public Map<String,Object> performanceAcceptance(@PathVariable("id") Long performanceId) throws Exception {

        try{
           return service.performanceAcceptance(performanceId);
        }catch (Exception e){
            return Map.of("result",e.getMessage());
        }
    }
    @GetMapping("/search")
    public Map<String,Object> performanceSearch(@RequestBody List<String> fields) throws Exception {

        try{
           return service.performanceSearch(fields);
        }catch (Exception e){
            return Map.of("result",e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public Map<String,Object> performanceView(@PathVariable("id") long performanceId){

        try{
           return service.performanceView(performanceId);
        }catch (Exception e){
            return Map.of("result",e.getMessage());
        }
    }

    private boolean artistBelongsToPerfomance(Long id,String user){
        var per = (PerformanceDto)service.performanceView(id).get("dtos");
        return per.getBandMembers().contains(user);
    }

}
