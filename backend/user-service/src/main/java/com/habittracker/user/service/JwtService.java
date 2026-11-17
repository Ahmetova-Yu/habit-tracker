package com.habittracker.user.service;

import com.habittracker.common.security.JwtClaims;
import com.habittracker.common.security.JwtProperties;
import com.habittracker.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties properties;

    public String generateAccessToken(User user) {
        return buildToken(user, properties.getAccessTtl(), "access");
    }

    public String generateRefreshToken(User user) {
        return buildToken(user, properties.getRefreshTtl(), "refresh");
    }

    private String buildToken(User user, Duration ttl, String type) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("role", user.getRole().name())
                .claim("type", type) // разделяем токены
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(ttl)))
                .signWith(getSigningKey())
                .compact();
    }

    public JwtClaims parseToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return new JwtClaims(
                Long.parseLong(claims.getSubject()),
                claims.get("email", String.class),
                claims.get("role", String.class),
                claims.get("type", String.class)
        );
    }

    public long accessTtlSeconds() {
        return properties.getAccessTtl().toSeconds();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8));
    }
}