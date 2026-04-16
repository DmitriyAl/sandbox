package io.microservices;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.clients.producer.Callback;

import java.util.Properties;
import java.util.Scanner;

public class KafkaProducerExample {
    private static final String TOPIC_NAME = "test-topic";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";

    public static void main(String[] args) {
        // Настройки producer
        Properties props = new Properties();
        props.put("bootstrap.servers", BOOTSTRAP_SERVERS);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("acks", "all"); // подтверждение от всех реплик
        props.put("retries", 3);   // количество попыток при ошибке

        // Создаем producer
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Kafka Producer запущен. Введите сообщения (exit - выход):");

            while (true) {
                System.out.print("> ");
                String message = scanner.nextLine();

                if ("exit".equalsIgnoreCase(message)) {
                    break;
                }

                // Отправляем сообщение
                ProducerRecord<String, String> record =
                        new ProducerRecord<>(TOPIC_NAME, message);

                producer.send(record, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception exception) {
                        if (exception == null) {
                            System.out.printf("✓ Сообщение отправлено: offset=%d, partition=%d%n",
                                    metadata.offset(), metadata.partition());
                        } else {
                            System.err.println("✗ Ошибка отправки: " + exception.getMessage());
                        }
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}