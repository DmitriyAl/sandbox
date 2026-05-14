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
    public ResponseEntity<?> sendMessages(@RequestParam("batches") int batches, @RequestParam("amount") int amount, @RequestParam("threads") int threads, @RequestParam("delay") int delay) {
        pressureService.sendMessages(batches, amount, threads, delay);
        return ResponseEntity.ok().build();
    }
}
