package com.habittracker.common.security;

public class JwtClaims {

    private final Long userId;
    private final String email;
    private final String role;

    public JwtClaims(Long userId, String email, String role) {
        this.userId = userId;
        this.email = email;
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}