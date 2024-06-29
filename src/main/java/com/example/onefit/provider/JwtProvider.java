package com.example.onefit.provider;

import com.example.onefit.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.StringJoiner;

@Component
public class JwtProvider {

    @Value("${jwt.token.expiration.mills}")
    @Getter
    private Long expiration;

    @Value("${jwt.token.secret.key}")
    private String secret;

    private SecretKey key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(UserEntity user) {
        StringJoiner joiner = new StringJoiner(",");
        user.getRoles().forEach(role -> joiner.add(role.getName()));
        return Jwts
                .builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .claim("id", user.getId())
                .claim("username", user.getUsername())
                .claim("roles", joiner.toString())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            final Claims claims = parseClaims(token);
            if (claims.getExpiration().before(new Date())) {
                return false;
            }
        } catch (JwtException e) {
            return false;
        }
        return true;
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
