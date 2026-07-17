// controller/InternalAuthController.java
package io.microservices.exchangetoken.targetapp.controller;

import io.microservices.exchangetoken.targetapp.config.props.SecurityProperties;
import io.microservices.exchangetoken.targetapp.dto.TokenResponse;
import io.microservices.exchangetoken.targetapp.service.OneTimeTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/auth")
@RequiredArgsConstructor
@Slf4j
public class InternalAuthController {

    private final OneTimeTokenService tokenService;

    private final SecurityProperties securityProperties;

    @PostMapping("/impersonate")
    public ResponseEntity<TokenResponse> impersonate(
            @RequestParam String userId,
            @RequestHeader(value = "X-Internal-Auth", required = false) String apiKey) {

        // Проверка внутреннего API-ключа
        if (apiKey == null || !apiKey.equals(securityProperties.getInternalApiKey())) {
            log.warn("Unauthorized attempt to impersonate userId: {}", userId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String refToken = tokenService.createToken(userId);
        return ResponseEntity.ok(new TokenResponse(refToken));
    }
}