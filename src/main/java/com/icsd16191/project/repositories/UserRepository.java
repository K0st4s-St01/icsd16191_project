package com.icsd16191.project.repositories;

import com.icsd16191.project.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    @Query("SELECT CASE WHEN :role IN (SELECT r FROM User u JOIN u.roles r WHERE u.username = :username) THEN true ELSE false END")
    boolean userHasRole(@Param("username") String username, @Param("role") String role);
}
