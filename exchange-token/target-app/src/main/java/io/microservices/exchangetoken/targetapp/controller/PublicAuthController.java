// controller/PublicAuthController.java
package io.microservices.exchangetoken.targetapp.controller;

import io.microservices.exchangetoken.targetapp.config.props.SecurityProperties;
import io.microservices.exchangetoken.targetapp.service.OneTimeTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
@Slf4j
public class PublicAuthController {

    private final OneTimeTokenService tokenService;

    private final SecurityProperties securityProperties;

    @GetMapping("/login-by-ref")
    public ResponseEntity<Void> loginByRef(
            @RequestParam("ref") String refToken,
            @RequestParam("redirect_uri") String redirectUri,
            HttpServletRequest request,
            HttpServletResponse response) {

        // 1. Валидация redirect_uri по белому списку
        boolean allowed = securityProperties.getAllowedRedirectPrefixes().stream()
                .anyMatch(redirectUri::startsWith);
        if (!allowed) {
            log.warn("Redirect URI not allowed: {}", redirectUri);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // 2. Потребление одноразового токена
        String userId = tokenService.consumeToken(refToken);
        if (userId == null) {
            log.warn("Invalid or expired ref token: {}", refToken);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 3. Аутентификация пользователя в системе Б (ручная установка SecurityContext)
        //    Здесь можно загрузить реальные роли из БД, для примера создаём простого пользователя
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userId, null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Сохраняем контекст в сессию (если используется SessionManagement)
        request.getSession().setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
        );

        // 4. Редирект на целевую страницу (уже без токена в URL)
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectUri));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}