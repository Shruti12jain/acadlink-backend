package com.acadlink.backend.utils;

import com.acadlink.backend.security.model.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    private static final String SECRET_KEY = 
            "this-is-a-very-long-and-secure-secret-key-used-for-hs256-jwt-signing"; // Must be ≥ 256 bits
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 1 day

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // Generate JWT
    public String generateToken(String userId, String email, Role role) {
        return Jwts.builder()
                .setSubject(userId)
                .claim("email", email)
                .claim("role", role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Extract username/email
    public String extractEmail(String token) {
        return extractAllClaims(token).get("email", String.class);
    }

    // Validate token
    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
