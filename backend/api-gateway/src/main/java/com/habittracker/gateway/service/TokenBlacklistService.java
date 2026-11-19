package com.habittracker.gateway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final ReactiveStringRedisTemplate redisTemplate;
    private static final String PREFIX = "blacklist:";

    // Добавляем токен в черный список на 1 час (или на время жизни токена)
    public Mono<Void> blacklist(String token) {
        return redisTemplate.opsForValue()
                .set(PREFIX + token, "revoked", Duration.ofHours(1))
                .then();
    }

    // Проверяем, есть ли токен в черном списке
    public Mono<Boolean> isBlacklisted(String token) {
        return redisTemplate.hasKey(PREFIX + token);
    }
}