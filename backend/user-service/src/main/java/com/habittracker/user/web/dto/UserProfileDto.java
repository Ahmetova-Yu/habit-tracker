package com.habittracker.user.web.dto;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class UserProfileDto {
    Long id;
    String username;
    String email;
    String role;
    String avatarUrl;
    Instant lastActiveAt;
}