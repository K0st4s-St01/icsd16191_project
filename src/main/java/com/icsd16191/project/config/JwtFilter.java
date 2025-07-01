package com.icsd16191.project.config;

import com.icsd16191.project.repositories.UserRepository;
import com.icsd16191.project.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService service;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var header = request.getHeader("Authorization");
        if(header !=null){
            var user = service.getUserFromAuthentication(request);
            var userRoles=userRepository.findById(user).orElseThrow().getRoles();
            var authorities = new ArrayList<FestivalAuthorities>();
            for (String role : userRoles){
                authorities.add(new FestivalAuthorities(role));
            }
            var authentication = new UsernamePasswordAuthenticationToken(user,null,authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request,response);
    }
    @AllArgsConstructor
    public static class FestivalAuthorities implements GrantedAuthority {
        private String role;
        @Override
        public String getAuthority() {
            return role;
        }
    }
}
