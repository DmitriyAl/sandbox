package io.microservices.producer.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class KafkaProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Counter succesCounter;
    private final Counter failureCounter;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate, MeterRegistry meterRegistry) {
        this.kafkaTemplate = kafkaTemplate;
        this.succesCounter = Counter.builder("kafka_producer_success_counter")
                .description("Success producer counter")
                .tags("producer", "success")
                .register(meterRegistry);
        this.failureCounter = Counter.builder("kafka_producer_failure_counter")
                .description("Failure producer counter")
                .tags("producer", "failure")
                .register(meterRegistry);
    }

    public void sendMessageSynchronously(String topic, int amount) {
        for (int i = 0; i < amount; i++) {
            sendMessage(topic, String.format("Message: %d", i));
        }
    }

    public void sendMessage(String topic, String message) {
        log.info("Sending message to topic {}: {}", topic, message);
        kafkaTemplate.send(topic, message)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Message sent successfully to topic {}", topic);
                        succesCounter.increment();
                    } else {
                        log.error("Failed to send message to topic {}", topic, ex);
                        failureCounter.increment();
                    }
                });
    }

    public void sendMessageAsynchronously(String topic, int amount, int threads, int delay) {
        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < amount; i++) {
            int step = i;
            executorService.execute(() -> sendMessage(topic, String.format("Message: %d", step)));
            if (delay > 0) {
                try {
                    TimeUnit.NANOSECONDS.sleep(delay);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}