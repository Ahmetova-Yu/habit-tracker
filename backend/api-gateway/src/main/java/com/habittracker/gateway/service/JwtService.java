package com.habittracker.gateway.service;

import com.habittracker.common.security.JwtClaims;
import com.habittracker.common.security.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties properties;

    public JwtClaims parseToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return new JwtClaims(
                Long.parseLong(claims.getSubject()),
                claims.get("email", String.class),
                claims.get("role", String.class)
        );
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8));
    }
}