package nsu.kochanov;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WarehouseTest {
    private Warehouse warehouse;

    @BeforeEach
    void setUp() {
        warehouse = new Warehouse();
    }

    @Test
    void testStoreAndTakeOrder() throws InterruptedException {
        Order order = new Order(1);

        // Добавляем заказ на склад
        warehouse.store(order);

        // Проверяем, что склад не пуст
        assertFalse(warehouse.isEmpty(), "Склад не должен быть пустым после добавления заказа");

        // Забираем заказ
        Order takenOrder = warehouse.take();
        assertEquals(1, takenOrder.getId(), "ID взятого заказа должен совпадать");

        // После забора склад должен быть пустым
        assertTrue(warehouse.isEmpty(), "Склад должен быть пуст после забора заказа");
    }

    @Test
    void testWaitsWhenWarehouseIsEmpty() throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                warehouse.take(); // Поток должен ждать, так как склад пуст
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        thread.start();
        Thread.sleep(500); // Даем время потоку уйти в ожидание

        assertTrue(thread.isAlive(), "Поток должен находиться в ожидании заказа");

        warehouse.store(new Order(2)); // Теперь поток должен продолжить работу
        thread.join(1000); // Ожидаем завершения потока

        assertFalse(thread.isAlive(), "Поток должен завершиться после получения заказа");
    }

    @Test
    void testWaitsWhenWarehouseIsFull() throws InterruptedException {
        // Заполняем склад
        for (int i = 1; i <= 5; i++) {
            warehouse.store(new Order(i));
        }

        Thread thread = new Thread(() -> {
            try {
                warehouse.store(new Order(6)); // Должен ждать, так как склад полон
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        thread.start();
        Thread.sleep(500); // Даем время потоку уйти в ожидание

        assertTrue(thread.isAlive(), "Поток должен находиться "
                + "в ожидании свободного места на складе");

        warehouse.take(); // Освобождаем место
        thread.join(1000); // Ожидаем, пока поток добавит заказ

        assertFalse(thread.isAlive(), "Поток должен завершиться после "
                + "освобождения места на складе");
    }
}