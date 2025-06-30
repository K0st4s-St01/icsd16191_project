package com.icsd16191.project.models.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User  {
    @Id
    private String username;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    @ManyToMany(mappedBy = "bandMembers")
    private List<Performance> performances = new ArrayList<>();

    @OneToOne(mappedBy = "staff")
    private Performance performance;

    @OneToMany(mappedBy = "organizer")
    private List<Festival> festivals = new ArrayList<>();
}
