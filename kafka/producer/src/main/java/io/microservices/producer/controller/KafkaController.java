package io.microservices.producer.controller;

import io.microservices.producer.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KafkaController {
    public static final String TOPIC = "test-topic";
    private final KafkaProducerService producerService;


    @PostMapping("/message")
    public void sendMessage(@RequestParam("message") String message) {
        producerService.sendMessage(TOPIC, String.format("Message: %s", message));
    }

    @PostMapping("/sync-pressure")
    public void addSyncPressure(@RequestParam("amount") int amount) {
        producerService.sendMessageSynchronously(TOPIC, amount);
    }

    @PostMapping("/async-pressure")
    public void addAsyncPressure(@RequestParam("amount") int amount, @RequestParam("threads") int threads) {
        producerService.sendMessageAsynchronously(TOPIC, amount, threads);
    }
}
