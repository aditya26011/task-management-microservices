package com.aditya.project_service.service;


import com.aditya.project_service.auth.AuthUser;
import com.aditya.project_service.entity.enums.Roles;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(AuthUser user){
      return   Jwts.builder()
                .subject(user.getId().toString())
                .claim("email",user.getEmail())
                .claim("role", user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000 *60*10))
                .signWith(getSecretKey())
                .compact();
    }
    public String generateRefreshToken(AuthUser user){
        return   Jwts.builder()
                .subject(user.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000L *60*60*24*30*6))
                .signWith(getSecretKey())
                .compact();
    }

    public Claims getClaims(String token){
        return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
    }

    public AuthUser getAuthUserFromToken(String token) {
        Claims claims = getClaims(token);
        String email = claims.get("email", String.class);
        String role = claims.get("role", String.class);
        Long id = Long.valueOf(claims.getSubject());

        AuthUser authUser = new AuthUser(
                id,
                email,
                Roles.valueOf(role)
        );



        return authUser;
    }
}
