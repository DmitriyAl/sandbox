package io.microservices.concurrency.reentrantlock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockProducerConsumer {
    private static final List<Integer> buffer = new ArrayList<>();
    private static final int CAPACITY = 5;

    // Создаем lock вместо synchronized
    private static final ReentrantLock lock = new ReentrantLock();

    // Создаем condition для координации потоков (аналог wait/notify)
    private static final Condition notFull = lock.newCondition();   // для producer
    private static final Condition notEmpty = lock.newCondition();  // для consumer

    static class Producer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                lock.lock(); // Вход в критическую секцию (аналог synchronized)
                try {
                    while (buffer.size() == CAPACITY) {
                        System.out.println("Producer ждет, буфер полон");
                        notFull.await(); // аналог wait()
                    }

                    buffer.add(i);
                    System.out.println("Produced: " + i + ", buffer size: " + buffer.size());

                    // Уведомляем consumer, что данные появились
                    notEmpty.signal(); // аналог notify()

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    lock.unlock(); // Обязательно освобождаем lock!
                }
            }
        }
    }

    static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    while (buffer.isEmpty()) {
                        System.out.println("Consumer ждет, буфер пуст");
                        notEmpty.await(); // ждем данные
                    }

                    int value = buffer.remove(0);
                    System.out.println("Consumed: " + value + ", buffer size: " + buffer.size());

                    // Уведомляем producer, что освободилось место
                    notFull.signal();

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static void main(String[] args) {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();
    }
}