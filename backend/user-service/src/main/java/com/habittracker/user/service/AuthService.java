package com.habittracker.user.service;

import com.habittracker.common.exception.BusinessException;
import com.habittracker.common.exception.UnauthorizedException;
import com.habittracker.common.security.JwtClaims;
import com.habittracker.user.domain.Role;
import com.habittracker.user.domain.User;
import com.habittracker.user.repository.UserRepository;
import com.habittracker.user.web.dto.AuthResponse;
import com.habittracker.user.web.dto.LoginRequest;
import com.habittracker.user.web.dto.RefreshRequest;
import com.habittracker.user.web.dto.RegisterRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already registered");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);
        return issueTokens(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        return issueTokens(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse refresh(RefreshRequest request) {
        try {
            JwtClaims claims = jwtService.parseToken(request.getRefreshToken());
            User user = userRepository.findById(claims.getUserId())
                    .orElseThrow(() -> new UnauthorizedException("User not found"));
            return issueTokens(user);
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid refresh token");
        }
    }

    private AuthResponse issueTokens(User user) {
        String access = jwtService.generateAccessToken(user);
        String refresh = jwtService.generateRefreshToken(user);
        return AuthResponse.of(access, refresh, jwtService.accessTtlSeconds());
    }
}