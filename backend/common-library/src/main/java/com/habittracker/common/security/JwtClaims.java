package com.habittracker.common.security;

public record JwtClaims(
        Long userId,
        String email,
        String role
) {}