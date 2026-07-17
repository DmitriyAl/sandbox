package io.microservices.exchangetoken.sourceapp.service;

import io.microservices.exchangetoken.sourceapp.component.LegacySystemClient;
import io.microservices.exchangetoken.sourceapp.config.props.LegacyProperties;
import io.microservices.exchangetoken.sourceapp.dto.LegacyTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BridgeService {

    private final LegacySystemClient legacySystemClient;
    private final LegacyProperties legacyProperties;

    public String buildRedirectUrl(String targetPath) {
        // 1. Валидация целевого пути по белому списку
        boolean allowed = legacyProperties.getAllowedRedirectPrefixes().stream()
                .anyMatch(targetPath::startsWith);
        if (!allowed) {
            throw new IllegalArgumentException("Target path not allowed: " + targetPath);
        }

        // 2. Получаем userId текущего пользователя (из Spring Security)
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        // 3. Запрашиваем одноразовый токен у системы Б
        LegacyTokenResponse response = legacySystemClient.requestOneTimeToken(userId);
        String refToken = response.getRefToken();

        // 4. Формируем URL для редиректа (без реального токена в теле ответа, только в Location)
        return legacyProperties.getBaseUrl() + "/public/login-by-ref?ref=" + refToken + "&redirect_uri=" + targetPath;
    }
}