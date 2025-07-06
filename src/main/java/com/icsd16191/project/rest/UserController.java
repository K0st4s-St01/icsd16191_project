package com.icsd16191.project.rest;

import com.icsd16191.project.models.dtos.UserDto;
import com.icsd16191.project.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/user")
public class UserController {
    private UserService service;
    @PostMapping
    public Map<String,Object> register(@RequestBody UserDto dto) throws Exception {
        try {
            return service.register(dto);
        }catch (Exception e){
            return Map.of("result",e.getMessage());
        }
    }
    @PostMapping("/login")
    public Map<String,Object> login(@RequestBody UserDto dto) throws Exception {
        try {
            return service.login(dto);
        }catch (Exception e){
            return Map.of("result",e.getMessage());
        }
    }
}
