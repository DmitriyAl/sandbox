package io.microservices.concurrency.wait;

public class SpuriousWakeupProblem {
    private static boolean condition = false;
    private static final Object lock = new Object();

    static class BadWaiter extends Thread {
        @Override
        public void run() {
            synchronized (lock) {
                // ПЛОХО: использование if вместо while
                if (!condition) {
                    try {
                        System.out.println(getName() + " начал ожидание");
                        lock.wait();
                        System.out.println(getName() + " проснулся");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Поток думает, что условие выполнено, но это не так!
                System.out.println(getName() + " ПРЕДПОЛАГАЕТ, что condition = true");
                System.out.println(getName() + " реальное condition = " + condition);

                if (condition) {
                    System.out.println(getName() + " выполняет действие...");
                } else {
                    System.out.println(getName() + " ОШИБКА: ложное пробуждение!");
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BadWaiter waiter = new BadWaiter();
        waiter.start();

        // Даем время потоку войти в wait
        Thread.sleep(1000);

        // НЕ вызываем notify() - просто ждем
        System.out.println("Основной поток: не вызываем notify()");
        Thread.sleep(5000);

        synchronized (lock) {
            lock.notify();
        }
        // Поток может проснуться сам без notify!
    }
}