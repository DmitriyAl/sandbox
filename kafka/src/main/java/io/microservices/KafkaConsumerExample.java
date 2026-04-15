package io.microservices;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class KafkaConsumerExample {
    private static final String TOPIC_NAME = "my-topic";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String GROUP_ID = "my-group";

    public static void main(String[] args) {
        // Настройки consumer
        Properties props = new Properties();
        props.put("bootstrap.servers", BOOTSTRAP_SERVERS);
        props.put("group.id", GROUP_ID);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("auto.offset.reset", "earliest"); // читать с начала
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");

        // Создаем consumer
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {

            // Подписываемся на топик
            consumer.subscribe(Arrays.asList(TOPIC_NAME));

            System.out.println("Kafka Consumer запущен. Ожидание сообщений...");
            System.out.println("Нажмите Ctrl+C для выхода\n");

            // Бесконечный цикл чтения сообщений
            while (true) {
                ConsumerRecords<String, String> records =
                        consumer.poll(Duration.ofMillis(1000));

                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf(
                            "[Получено] topic=%s, partition=%d, offset=%d, key=%s, value=%s%n",
                            record.topic(), record.partition(), record.offset(),
                            record.key(), record.value()
                    );
                }
                consumer.commitAsync();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}