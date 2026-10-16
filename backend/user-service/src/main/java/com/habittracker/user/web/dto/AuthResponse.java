package com.habittracker.user.web.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AuthResponse {

    String accessToken;
    String refreshToken;
    String tokenType;
    long expiresIn;

    public static AuthResponse of(String access, String refresh, long ttlSeconds) {
        return AuthResponse.builder()
                .accessToken(access)
                .refreshToken(refresh)
                .tokenType("Bearer")
                .expiresIn(ttlSeconds)
                .build();
    }
}