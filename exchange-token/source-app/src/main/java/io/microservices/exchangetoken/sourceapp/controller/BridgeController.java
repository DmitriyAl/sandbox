package io.microservices.exchangetoken.sourceapp.controller;

import io.microservices.exchangetoken.sourceapp.dto.RedirectRequest;
import io.microservices.exchangetoken.sourceapp.service.BridgeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/bridge")
@RequiredArgsConstructor
public class BridgeController {

    private final BridgeService bridgeService;

    @PostMapping("/redirect")
    public ResponseEntity<Void> redirectToLegacy(@Valid @RequestBody RedirectRequest request) {
        // Получаем полный URL для редиректа (уже содержащий одноразовый токен)
        String redirectUrl = bridgeService.buildRedirectUrl(request.getTarget());
        // Возвращаем 302 Found с заголовком Location
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}