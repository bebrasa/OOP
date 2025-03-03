package nsu.kochanov;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Загрузка конфигурации
        Config config = Config.loadConfig("pizzeria_config.json");
        if (config == null) {
            System.out.println("Ошибка загрузки конфигурации.");
            return;
        }

        // Создание пиццерии
        Pizzeria pizzeria = new Pizzeria(config);

        // Запуск потока для генерации заказов
        Thread orderGenerator = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                pizzeria.placeOrder(new Order());
                try {
                    Thread.sleep(5000); // Добавляем заказ каждые 500 мс
                } catch (InterruptedException e) {
                    System.out.println("Генерация заказов остановлена.");
                    break;
                }
            }
        });

        orderGenerator.start();

        // Запуск пиццерии
        pizzeria.start();

        // Ожидание команды для завершения работы
        System.out.println("Введите 'exit' для завершения работы");
        Scanner scanner = new Scanner(System.in);
        while (!scanner.nextLine().equalsIgnoreCase("exit")) {
            System.out.println("Неверная команда");
        }
        scanner.close();

        // Остановка генерации заказов
        orderGenerator.interrupt();
        try {
            orderGenerator.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Остановка пиццерии
        pizzeria.stop();
    }
}