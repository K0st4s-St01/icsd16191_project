package com.icsd16191.project.rest;

import com.icsd16191.project.models.dtos.FestivalDto;
import com.icsd16191.project.services.FestivalService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/festival")
@AllArgsConstructor
public class FestivalRestController {
    private FestivalService festivalService;
    @PostMapping
    public Map<String,Object> festivalCreation(@RequestBody FestivalDto dto)  {
        try {
            String user = SecurityContextHolder.getContext().getAuthentication().getName();
            return festivalService.festivalCreation(user, dto);
        }catch (Exception e){
            return Map.of("result",e.getMessage());
        }
    }
    @PutMapping("/{festivalId}")
    public Map<String,Object> festivalUpdate(@RequestBody FestivalDto dto,@PathVariable("festivalId") Long festivalId)  {
        try {
            String user = SecurityContextHolder.getContext().getAuthentication().getName();
            return festivalService.festivalUpdate(user, dto,festivalId);
        }catch (Exception e){
            return Map.of("result",e.getMessage());
        }
    }
    @PutMapping("/{id}/organizers_add")
    public Map<String,Object> organizersAddition(@PathVariable("id") Long id,@RequestBody List<String> organizersNew)  {
        try {
            String user = SecurityContextHolder.getContext().getAuthentication().getName();
            return festivalService.organizersAddition(id,user,organizersNew);
        }catch (Exception e){
            return Map.of("result",e.getMessage());
        }
    }
    @PutMapping("/{id}/staff_add")
    public Map<String,Object> staffAddition(@PathVariable("id") Long id,@RequestBody List<String> staffNew)  {
        try {
            String user = SecurityContextHolder.getContext().getAuthentication().getName();
            return festivalService.staffAddition(id,user,staffNew);
        }catch (Exception e){
            return Map.of("result",e.getMessage());
        }
    }
    @GetMapping("/search")
    public Map<String,Object> festivalSearch(@RequestBody List<String> keywords){
        try {
            return festivalService.festivalSearch(keywords);
        }catch (Exception e){
            return Map.of("result",e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public Map<String,Object> festivalView(@PathVariable("id") Long id){
        try {
            return festivalService.festivalView(id);
        }catch (Exception e){
            return Map.of("result",e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public Map<String,Object> festivalDelete(@PathVariable("id") Long id)  {
        try {
            String user = SecurityContextHolder.getContext().getAuthentication().getName();
            return festivalService.festivalDelete(id,user);
        }catch (Exception e){
            return Map.of("result",e.getMessage());
        }
    }
    @PostMapping("/submission_start/{id}")
    public Map<String,Object> submissionStart(@PathVariable("id") Long id)  {

        try {
            String user = SecurityContextHolder.getContext().getAuthentication().getName();
            return festivalService.submissionStart(id,user);
        }catch (Exception e){
            return Map.of("result",e.getMessage());
        }
    }
    @PostMapping("/assignment_start/{id}")
    public Map<String,Object> assignmentStart(@PathVariable("id") Long id)  {

        try {
            String user = SecurityContextHolder.getContext().getAuthentication().getName();
            return festivalService.assignmentStart(id,user);
        }catch (Exception e){
            return Map.of("result",e.getMessage());
        }
    }
    @PostMapping("/review_start/{id}")
    public Map<String,Object> reviewStart(@PathVariable("id") Long id)  {

        try {
            String user = SecurityContextHolder.getContext().getAuthentication().getName();
            return festivalService.reviewStart(id,user);
        }catch (Exception e){
            return Map.of("result",e.getMessage());
        }
    }
    @PostMapping("/scheduling/{id}")
    public Map<String,Object> schedulingState(@PathVariable("id") Long id)  {

        try {
            String user = SecurityContextHolder.getContext().getAuthentication().getName();
            return festivalService.schedulingState(id,user);
        }catch (Exception e){
            return Map.of("result",e.getMessage());
        }
    }
    @PostMapping("/final_submission/{id}")
    public Map<String,Object> finalSubmissionState(@PathVariable("id") Long id)  {

        try {
            String user = SecurityContextHolder.getContext().getAuthentication().getName();
            return festivalService.finalSubmissionState(id,user);
        }catch (Exception e){
            return Map.of("result",e.getMessage());
        }
    }
    @PostMapping("/decision/{id}")
    public Map<String,Object> decisionState(@PathVariable("id") Long id)  {

        try {
            String user = SecurityContextHolder.getContext().getAuthentication().getName();
            return festivalService.decisionState(id,user);
        }catch (Exception e){
            return Map.of("result",e.getMessage());
        }
    }
    @PostMapping("/announcement/{id}")
    public Map<String,Object> announcementState(@PathVariable("id") Long id)  {

        try {
            String user = SecurityContextHolder.getContext().getAuthentication().getName();
            return festivalService.announcementState(id,user);
        }catch (Exception e){
            return Map.of("result",e.getMessage());
        }
    }
}
