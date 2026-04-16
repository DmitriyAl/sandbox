package io.microservices.consumer.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageConsumerService {

    /**
     * Слушатель для топика test-topic
     *
     * @param record полученное сообщение
     * @param acknowledgment объект для ручного подтверждения
     */
    @KafkaListener(
            topics = {"test-topic", "my-app-logs"},  // Слушаем оба топика
            groupId = "my-consumer-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        log.info("=".repeat(60));
        log.info("📨 Получено сообщение:");
        log.info("   Топик: {}", record.topic());
        log.info("   Партиция: {}", record.partition());
        log.info("   Offset: {}", record.offset());
        log.info("   Ключ: {}", record.key());
        log.info("   Значение: {}", record.value());
        log.info("   Timestamp: {}", record.timestamp());
        log.info("=".repeat(60));

        try {
            // Здесь ваша бизнес-логика обработки сообщения
            processMessage(record.value());

            // Ручное подтверждение успешной обработки
            acknowledgment.acknowledge();
            log.info("✅ Сообщение успешно обработано и подтверждено");
        } catch (Exception e) {
            log.error("❌ Ошибка при обработке сообщения: {}", e.getMessage(), e);
            // В зависимости от требований: можно не подтверждать (сообщение будет перечитано)
            // или сохранить в DLQ (Dead Letter Queue)
        }
    }

    /**
     * Альтернативный слушатель с автоматической конвертацией в JSON
     */
    @KafkaListener(
            topics = "test-topic",
            groupId = "json-consumer-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeJson(String message, Acknowledgment acknowledgment) {
        log.info("📝 JSON сообщение: {}", message);
        // Здесь можно использовать ObjectMapper для парсинга
        acknowledgment.acknowledge();
    }

    private void processMessage(String message) {
        // Имитация бизнес-логики
        if (message != null && message.contains("error")) {
            throw new RuntimeException("Тестовая ошибка обработки");
        }
        log.info("🔧 Обработка сообщения: {}", message.toUpperCase());
    }
}