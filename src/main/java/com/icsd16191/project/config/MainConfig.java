package com.icsd16191.project.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

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
                            .requestMatchers(HttpMethod.GET, "/festival/search", "/festival/*").permitAll()

                            .requestMatchers(HttpMethod.POST, "/festival").hasAuthority("ORGANIZER")
                            .requestMatchers(HttpMethod.PUT, "/festival/*").hasAuthority("ORGANIZER")
                            .requestMatchers(HttpMethod.PUT, "/festival/*/organizers_add").hasAuthority("ORGANIZER")
                            .requestMatchers(HttpMethod.PUT, "/festival/*/staff_add").hasAuthority("ORGANIZER")
                            .requestMatchers(HttpMethod.DELETE, "/festival/*").hasAuthority("ORGANIZER")
                            .requestMatchers(HttpMethod.POST, "/festival/submission_start/*").hasAuthority("ORGANIZER")
                            .requestMatchers(HttpMethod.POST, "/festival/assignment_start/*").hasAuthority("ORGANIZER")
                            .requestMatchers(HttpMethod.POST, "/festival/review_start/*").hasAuthority("ORGANIZER")
                            .requestMatchers(HttpMethod.POST, "/festival/scheduling/*").hasAuthority("ORGANIZER")
                            .requestMatchers(HttpMethod.POST, "/festival/final_submission/*").hasAuthority("ORGANIZER")
                            .requestMatchers(HttpMethod.POST, "/festival/decision/*").hasAuthority("ORGANIZER")
                            .requestMatchers(HttpMethod.POST, "/festival/announcement/*").hasAuthority("ORGANIZER")

                            .requestMatchers(HttpMethod.GET, "/performance/search", "/performance/*").permitAll()

                            .requestMatchers(HttpMethod.POST, "/performance").hasAuthority("ARTIST")
                            .requestMatchers(HttpMethod.PUT, "/performance/*").hasAuthority("ARTIST")
                            .requestMatchers(HttpMethod.POST, "/performance/*/member/*").hasAuthority("ARTIST")
                            .requestMatchers(HttpMethod.POST, "/performance/*/submit/").hasAuthority("ARTIST")
                            .requestMatchers(HttpMethod.DELETE, "/performance/*").hasAuthority("ARTIST")
                            .requestMatchers(HttpMethod.POST, "/performance/*/final_submit").hasAuthority("ARTIST")

                            .requestMatchers(HttpMethod.POST, "/performance/*/staff/*").hasAuthority("ORGANIZER")

                            .requestMatchers(HttpMethod.POST, "/performance/*/staff/*/review").hasAuthority("STAFF")
                            .requestMatchers(HttpMethod.DELETE, "/performance/*/staff/rejection").hasAuthority("STAFF")
                            .requestMatchers(HttpMethod.POST, "/performance/*/staff/accept").hasAuthority("STAFF")

                            .anyRequest().authenticated();
                }
        );
        httpSecurity.formLogin(l -> l.disable());
        httpSecurity.authenticationProvider(provider);
        httpSecurity.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    private static class OrganizerOnlyRequestMatcher implements RequestMatcher {

        @Override
        public boolean matches(HttpServletRequest request) {
            String method = request.getMethod();
            String path = request.getRequestURI();

            if ("GET".equalsIgnoreCase(method)) {
                if (path.matches("^/festival/search/?$")) return false;
                if (path.matches("^/festival/\\d+/?$")) return false;
            }

            return path.startsWith("/festival/");
        }
    }

}
