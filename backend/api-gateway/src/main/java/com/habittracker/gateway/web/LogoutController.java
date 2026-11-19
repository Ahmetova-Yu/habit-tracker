package com.habittracker.gateway.web;

import com.habittracker.common.security.SecurityConstants;
import com.habittracker.gateway.service.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LogoutController {

    private final TokenBlacklistService blacklistService;

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> logout(@RequestHeader(SecurityConstants.AUTHORIZATION_HEADER) String authHeader) {
        if (authHeader != null && authHeader.startsWith(SecurityConstants.BEARER_PREFIX)) {
            String token = authHeader.substring(SecurityConstants.BEARER_PREFIX.length());
            return blacklistService.blacklist(token); // <--- ВОЗВРАЩАЕМ Mono!
        }
        return Mono.empty();
    }
}