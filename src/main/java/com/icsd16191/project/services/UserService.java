package com.icsd16191.project.services;

import com.icsd16191.project.config.FestivalAuthProvider;
import com.icsd16191.project.mappers.UserMapper;
import com.icsd16191.project.models.dtos.UserDto;
import com.icsd16191.project.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@AllArgsConstructor
@Service
public class UserService {
    private UserRepository userRepository;
    private UserMapper userMapper;
    private BCryptPasswordEncoder passwordEncoder;
    private FestivalAuthProvider authProvider;
    private JwtService service;

    public Map<String,Object> register(UserDto dto) throws Exception {
        if (dto.getRoles().isEmpty()){
            throw new Exception("no roles in dto");
        }
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        if (!userRepository.existsById(dto.getUsername())) {
            var entity = userMapper.toEntity(dto
                    ,dto.getRoles()
                    ,new ArrayList<>()
                    ,new ArrayList<>()
                    ,new ArrayList<>()
                    ,null
                    );
            userRepository.save(entity);
            return Map.of("result","successfully added "+dto.getUsername()+" "+dto.getRoles().get(0));
        }
        return Map.of("result","username "+dto.getUsername()+" taken");
    }
    public Map<String,Object> login(UserDto dto) throws Exception {
        var auth = authProvider.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(),dto.getPassword()));
        if (auth!=null){
            return Map.of("result","successful","token",service.getToken(auth.getName()));
        }else{
            throw new Exception("wrong username or password");
        }
    }
}
