package com.icsd16191.project.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class MainConfig {
    private JwtFilter filter;
    private FestivalAuthProvider provider;
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable());
        httpSecurity.sessionManagement(
                s ->  s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        httpSecurity.authorizeHttpRequests(http -> {
                    http.requestMatchers("/user/**").permitAll()
                            .requestMatchers("/festival/**").hasAuthority("ORGANIZER");
                }
        );
        httpSecurity.formLogin(l -> l.disable());
        httpSecurity.authenticationProvider(provider);
        httpSecurity.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
