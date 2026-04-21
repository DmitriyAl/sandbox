package io.microservices.pressure.controller;

import io.microservices.pressure.service.PressureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PressureController {
    private final PressureService pressureService;

    @PostMapping("pressure")
    public ResponseEntity<?> sendMessages(@RequestParam int amount) {
        pressureService.sendMessages(amount);
        return ResponseEntity.ok().build();
    }
}
