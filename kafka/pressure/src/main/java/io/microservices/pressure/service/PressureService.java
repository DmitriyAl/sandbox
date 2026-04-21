package io.microservices.pressure.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class PressureService {
    private static final String TARGET_URL = "http://localhost:18081";
    private final WebClient webClient;

    public PressureService() {
        this.webClient = WebClient.builder()
                .baseUrl(TARGET_URL)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public void sendMessages(int amount) {
        for (int i = 0; i < amount; i++) {
            int param = i;
            webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/message")
                            .queryParam("message", param)
                            .build()) // пустая строка, так как baseUrl уже задан
                    .retrieve()
                    .bodyToMono(Object.class)
                    .doOnSuccess(response -> log.info("Request {} success", param))
                    .doOnError(error -> log.error("Request {} failed", param, error))
                    .subscribe();
        }
        log.info("{} messages were sent", amount);
    }
}
