package io.microservices.producer.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

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
}