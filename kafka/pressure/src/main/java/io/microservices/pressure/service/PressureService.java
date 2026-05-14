package io.microservices.pressure.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class PressureService {
    private static final String TARGET_URL = "http://127.0.0.1:52091";
    private final WebClient webClient;

    public PressureService() {
        this.webClient = WebClient.builder()
                .baseUrl(TARGET_URL)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public void sendMessages(int batches, int amount, int threads) {
        for (int i = 0; i < batches; i++) {
            webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/async-pressure")
                            .queryParam("amount", amount)
                            .queryParam("threads", threads)
                            .build()) // пустая строка, так как baseUrl уже задан
                    .retrieve()
                    .bodyToMono(Object.class)
                    .doOnSuccess(response -> log.info("Request on {} messages bu {} threads successfully done", amount, threads))
                    .doOnError(error -> log.error("Request on {} messages bu {} threads failed", amount, threads, error))
                    .subscribe();
        log.info("{} messages were sent", amount);
        }
    }
}
