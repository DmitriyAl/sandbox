package io.microservices.exchangetoken.targetapp.service;

import io.microservices.exchangetoken.targetapp.config.props.SecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OneTimeTokenService {

    private final StringRedisTemplate redisTemplate;

    private final SecurityProperties securityProperties;

    public String createToken(String userId) {
        String token = UUID.randomUUID().toString();
        // Сохраняем userId по ключу токена
        redisTemplate.opsForValue().set("ref:" + token, userId, securityProperties.getTokenTtlSeconds(), TimeUnit.SECONDS);
        return token;
    }

    public String consumeToken(String token) {
        String key = "ref:" + token;
        String userId = redisTemplate.opsForValue().get(key);
        if (userId == null) {
            return null;
        }
        // Удаляем токен (одноразовость)
        redisTemplate.delete(key);
        return userId;
    }
}