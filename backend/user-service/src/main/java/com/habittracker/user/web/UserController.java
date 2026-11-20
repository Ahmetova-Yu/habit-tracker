package com.habittracker.user.web;

import com.habittracker.common.security.SecurityConstants;
import com.habittracker.user.service.AuthService;
import com.habittracker.user.service.UserService;
import com.habittracker.user.web.dto.ChangePasswordRequest;
import com.habittracker.user.web.dto.UpdateProfileRequest;
import com.habittracker.user.web.dto.UserProfileDto;
import com.habittracker.user.web.dto.UserSearchDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;
    private final UserService userService;

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

    @PatchMapping("/me/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(
            @RequestHeader(SecurityConstants.USER_ID_HEADER) Long userId,
            @Valid @RequestBody ChangePasswordRequest request) {
        authService.changePassword(userId, request);
    }

    @PostMapping("/me/heartbeat")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void heartbeat(@RequestHeader(SecurityConstants.USER_ID_HEADER) Long userId) {
        authService.updateLastActive(userId);
    }

    @GetMapping("/search")
    public List<UserSearchDto> searchUsers(
            @RequestHeader(SecurityConstants.USER_ID_HEADER) Long userId,
            @RequestParam String query) {
        return userService.searchUsers(query, userId);
    }
}