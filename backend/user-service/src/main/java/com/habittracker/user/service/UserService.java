package com.habittracker.user.service;

import com.habittracker.common.exception.BusinessException;
import com.habittracker.user.repository.UserRepository;
import com.habittracker.user.web.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional(readOnly = true)
    public List<UserSearchDto> searchUsers(String query, Long currentUserId) {
        if (query == null || query.trim().length() < 2) {
            throw new BusinessException("Search query must be at least 2 characters");
        }
        return userRepository.searchByUsernameExcludingUser(query.trim(), currentUserId)
                .stream()
                .map(u -> new UserSearchDto(u.getId(), u.getUsername(), u.getAvatarUrl()))
                .toList();
    }
}