package io.microservices.concurrency.wait;

import java.util.ArrayList;
import java.util.List;

public class WithoutWaitNotify {
    private static List<Integer> buffer = new ArrayList<>();
    private static final int CAPACITY = 5;

    static class Producer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (buffer) {
                    if (buffer.size() == CAPACITY) {
                        // busy waiting — ПЛОХО! Потребляет CPU
                        continue;
                    }
                    buffer.add(i);
                    System.out.println("Produced: " + i);
                }
            }
        }
    }

    // Проблема: постоянная проверка в цикле, пустая трата CPU
}