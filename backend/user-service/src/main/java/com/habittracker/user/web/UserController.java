package com.habittracker.user.web;

import com.habittracker.common.security.SecurityConstants;
import com.habittracker.user.service.AuthService;
import com.habittracker.user.web.dto.UpdateProfileRequest;
import com.habittracker.user.web.dto.UserProfileDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    @GetMapping("/me")
    public UserProfileDto getMyProfile(
            @RequestHeader(SecurityConstants.USER_ID_HEADER) Long userId) {
        return authService.getProfile(userId);
    }

    @PatchMapping("/me")
    public UserProfileDto updateMyProfile(
            @RequestHeader(SecurityConstants.USER_ID_HEADER) Long userId,
            @Valid @RequestBody UpdateProfileRequest request) {
        return authService.updateProfile(userId, request);
    }
}