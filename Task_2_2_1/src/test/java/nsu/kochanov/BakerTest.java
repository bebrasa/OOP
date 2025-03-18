package nsu.kochanov;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BakerTest {
    private OrderQueue orderQueue;
    private Warehouse warehouse;
    private Baker baker;
    private Thread bakerThread;

    @BeforeEach
    void setUp() {
        orderQueue = new OrderQueue();
        warehouse = new Warehouse();
        baker = new Baker(1, orderQueue, warehouse);
        bakerThread = new Thread(baker);
    }

    @Test
    void testBakerProcessesOrder() throws InterruptedException {
        // Добавляем заказ в очередь
        Order order = new Order(1);
        orderQueue.addOrder(order);

        // Запускаем пекаря
        bakerThread.start();
        Thread.sleep(3000); // Даем время на обработку заказа

        // Проверяем, что заказ попал на склад
        assertFalse(warehouse.isEmpty(), "Склад не должен быть пустым");
        assertEquals(order.getId(), warehouse.take().getId(), "ID заказа должен совпадать");

        // Завершаем работу пекаря
        bakerThread.interrupt();
        bakerThread.join();
    }

    @Test
    void testBakerStopsOnInterrupt() throws InterruptedException {
        // Запускаем пекаря без заказов
        bakerThread.start();
        Thread.sleep(1000); // Ждем, чтобы поток вошел в ожидание

        // Прерываем пекаря
        bakerThread.interrupt();
        bakerThread.join();

        // Проверяем, что после прерывания склад пуст
        assertTrue(warehouse.isEmpty(), "Склад должен оставаться пустым");
    }
}