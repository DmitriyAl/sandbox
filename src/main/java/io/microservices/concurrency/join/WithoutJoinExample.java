package io.microservices.concurrency.join;

public class WithoutJoinExample {
    private static int result = 0;

    public static void main(String[] args) {
        Thread worker = new Thread(() -> {
            // Имитация долгой операции
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            result = 42; // Результат вычислений
            System.out.println("Worker завершил вычисления");
        });
        worker.setDaemon(true);
        worker.start();

        // Главный поток продолжает выполнение без ожидания
        System.out.println("Результат: " + result); // Выведет 0!
        System.out.println("Программа завершена");
    }
}
