package io.microservices.exchangetoken.sourceapp.controller;

import io.microservices.exchangetoken.sourceapp.dto.LoginRequest;
import io.microservices.exchangetoken.sourceapp.dto.LoginResponse;
import io.microservices.exchangetoken.sourceapp.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.microservices.exchangetoken.sourceapp.constant.Constants.DEMO_PASSWORD_HASH;
import static io.microservices.exchangetoken.sourceapp.constant.Constants.DEMO_USER;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        // Проверка логина
        if (!DEMO_USER.equals(request.getUsername())) {
            return ResponseEntity.status(401).build();
        }
        // Проверка пароля
        if (!passwordEncoder.matches(request.getPassword(), DEMO_PASSWORD_HASH)) {
            return ResponseEntity.status(401).build();
        }

        String token = jwtUtil.generateToken(request.getUsername());
        log.info("User {} logged in successfully", request.getUsername());
        return ResponseEntity.ok(new LoginResponse(token));
    }
}