package io.microservices.producer.controller;

import io.microservices.producer.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KafkaController {
    private final KafkaProducerService producerService;


    @PostMapping("/message")
    public void sendMessage(@RequestParam("message") String message) {
        producerService.sendMessage("test-topic", String.format("Message: %s", message));
    }
}
