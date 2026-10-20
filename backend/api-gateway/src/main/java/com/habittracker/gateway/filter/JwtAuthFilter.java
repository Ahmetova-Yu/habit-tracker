package com.habittracker.gateway.filter;

import com.habittracker.common.security.JwtClaims;
import com.habittracker.common.security.SecurityConstants;
import com.habittracker.gateway.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter implements GlobalFilter, Ordered {

    private final JwtService jwtService;

    // Пути, которые не требуют токена
    private final List<String> publicPaths = List.of(
            "/api/auth/register",
            "/api/auth/login",
            "/api/auth/refresh"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();

        // 1. Пропускаем публичные пути
        if (publicPaths.stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }

        // 2. Проверяем наличие токена
        String authHeader = request.getHeaders().getFirst(SecurityConstants.AUTHORIZATION_HEADER);
        if (authHeader == null || !authHeader.startsWith(SecurityConstants.BEARER_PREFIX)) {
            return unauthorized(exchange, "Missing or invalid Authorization header");
        }

        String token = authHeader.substring(SecurityConstants.BEARER_PREFIX.length());

        // 3. Валидируем токен
        try {
            JwtClaims claims = jwtService.parseToken(token);

            // 4. Прокидываем данные юзера дальше в сервисы через заголовки
            ServerHttpRequest mutatedRequest = request.mutate()
                    .header(SecurityConstants.USER_ID_HEADER, claims.userId().toString())
                    .header(SecurityConstants.USER_EMAIL_HEADER, claims.email())
                    .header(SecurityConstants.USER_ROLE_HEADER, claims.role())
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());

        } catch (ExpiredJwtException e) {
            return unauthorized(exchange, "Token expired");
        } catch (JwtException e) {
            return unauthorized(exchange, "Invalid token");
        }
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String body = "{\"error\":\"unauthorized\",\"message\":\"" + message + "\"}";
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return -100; // Выполнять до остальных фильтров
    }
}