package io.microservices.exchangetoken.sourceapp.controller;

import io.microservices.exchangetoken.sourceapp.dto.RedirectRequest;
import io.microservices.exchangetoken.sourceapp.service.BridgeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/bridge")
@RequiredArgsConstructor
public class BridgeController {

    private final BridgeService bridgeService;

    @GetMapping("/redirect")
    public ResponseEntity<Void> redirectToLegacy(@RequestParam("target") String target) {
        String redirectUrl = bridgeService.buildRedirectUrl(target);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND); // 302
    }
}