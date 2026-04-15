package io.microservices.concurrency.join;

public class WithJoinExample {
    private static int result = 0;

    public static void main(String[] args) {
        Thread worker = new Thread(() -> {
            try {
                Thread.sleep(2000); // Имитация сложных вычислений
                result = 42;
                System.out.println("Worker завершил вычисления");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        worker.start();

        try {
            worker.join(); // Главный поток ждет завершения worker
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Теперь результат гарантированно доступен
        System.out.println("Результат: " + result); // Выведет 42
        System.out.println("Программа завершена");
    }
}