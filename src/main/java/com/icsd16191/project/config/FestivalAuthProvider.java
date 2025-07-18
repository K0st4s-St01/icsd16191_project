package com.icsd16191.project.config;

import com.icsd16191.project.mappers.UserMapper;
import com.icsd16191.project.models.dtos.UserDto;
import com.icsd16191.project.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

@Component
@AllArgsConstructor
public class FestivalAuthProvider implements AuthenticationProvider {
    private UserRepository userRepository;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        var entity = userRepository.findById(username);
        if(entity.isPresent()){
            var entityObj = entity.get();
            if(BCrypt.checkpw(
                    authentication.getCredentials().toString(),
                    entityObj.getPassword()
            )){
                var authorities = new ArrayList<JwtFilter.FestivalAuthorities>();
                for (String role : entityObj.getRoles()){
                    authorities.add(new JwtFilter.FestivalAuthorities(role));
                }
                var auth_token = new UsernamePasswordAuthenticationToken(entityObj.getUsername(),entityObj.getPassword(),authorities);
                return auth_token;
            }
        } else {
            throw new RuntimeException("no such user");
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
