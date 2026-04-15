package io.microservices.concurrency.interrupt;

public class InterruptExample {

    static class LongRunningTask extends Thread {
        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    // Выполняем полезную работу
                    doWork();

                    // Если работа быстрая, проверяем флаг
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("Получен сигнал прерывания, завершаемся");
                        break;
                    }
                }
            } catch (InterruptedException e) {
                // Методы sleep/wait/join выбрасывают InterruptedException
                // и СБРАСЫВАЮТ флаг прерывания
                System.out.println("Был прерван во время ожидания");
                // Восстанавливаем статус прерывания для вышестоящих методов
                Thread.currentThread().interrupt();
            }
        }

        private void doWork() throws InterruptedException {
            // Имитация работы с возможным ожиданием
            Thread.sleep(100); // Может выбросить InterruptedException
        }
    }

    public static void main(String[] args) throws InterruptedException {
        LongRunningTask task = new LongRunningTask();
        task.start();

        // Через 2 секунды прерываем
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        task.interrupt();
        System.out.println("Отправлен сигнал прерывания");
        task.join();
    }
}
