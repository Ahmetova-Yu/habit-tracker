package com.habittracker.user.web.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileRequest {
    @Size(max = 50, message = "Username must be less than 50 characters")
    private String username;

    @Size(max = 500, message = "Avatar URL must be less than 500 characters")
    private String avatarUrl;
}