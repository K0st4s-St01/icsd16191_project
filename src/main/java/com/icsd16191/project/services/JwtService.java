package com.icsd16191.project.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtService {
    private long EXPIRATIONTIME=8640000;
    private String prefix = "Bearer";
    private SecretKey key = Keys.hmacShaKeyFor("IDontKnowWhyLibrariesChangeEverythingOvertime!!1998".getBytes(StandardCharsets.UTF_8));

    public String getToken(String username){
        return Jwts.builder()
                .subject(username)
                .expiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                .signWith(key)
                .compact();
    }
    public String getUserFromAuthentication(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String user = null;
        if (token!=null){
            user = Jwts.parser()
                    .decryptWith(key)
                    .build()
                    .parseSignedClaims(token.replace(prefix,""))
                    .getPayload()
                    .getSubject();

        }
        return user;

    }
}
