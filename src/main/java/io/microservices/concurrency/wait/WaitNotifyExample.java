package io.microservices.concurrency.wait;

import java.util.ArrayList;
import java.util.List;

public class WaitNotifyExample {
    private static List<Integer> buffer = new ArrayList<>();
    private static final int ITERATIONS = 1000;

    static class Producer1 extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < ITERATIONS; i++) {
                synchronized (buffer) {
                    while (buffer.size() == ITERATIONS/2) { // wait пока не освободится место
                        try {
                            System.out.println("Producer2 waiting, buffer full");
                            buffer.wait(); // освобождает монитор и ждет
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    buffer.add(i);
                    System.out.println("Produced1: " + i + ", buffer size: " + buffer.size());
                    buffer.notifyAll(); // уведомляем consumer, что данные появились
                }
            }
        }
    }

    static class Producer2 extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < ITERATIONS; i++) {
                synchronized (buffer) {
                    while (buffer.size() == ITERATIONS/2) { // wait пока не освободится место
                        try {
                            System.out.println("Producer2 waiting, buffer full");
                            buffer.wait(); // освобождает монитор и ждет
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    buffer.add(i);
                    System.out.println("Produced2: " + i + ", buffer size: " + buffer.size());
                    buffer.notifyAll(); // уведомляем consumer, что данные появились
                }
            }
        }
    }

    static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < ITERATIONS; i++) {
                synchronized (buffer) {
                    while (buffer.isEmpty()) { // wait пока нет данных
                        try {
                            System.out.println("Consumer waiting, buffer empty");
                            buffer.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    int value = buffer.remove(0);
                    System.out.println("Consumed: " + value + ", buffer size: " + buffer.size());
                    buffer.notifyAll(); // уведомляем producer, что освободилось место
                }
            }
        }
    }

    public static void main(String[] args) {
        Producer1 producer1 = new Producer1();
        Producer2 producer2 = new Producer2();
        Consumer consumer = new Consumer();

        producer1.start();
        producer2.start();
        consumer.start();
    }
}