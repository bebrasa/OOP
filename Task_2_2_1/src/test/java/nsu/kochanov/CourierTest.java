package nsu.kochanov;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CourierTest {
    private Warehouse warehouse;
    private Courier courier;
    private Thread courierThread;

    @BeforeEach
    void setUp() {
        warehouse = new Warehouse();
        courier = new Courier(1, warehouse);
        courierThread = new Thread(courier);
    }

    @Test
    void testCourierDeliversOrder() throws InterruptedException {
        // Добавляем заказ на склад
        Order order = new Order(1);
        warehouse.store(order);

        // Запускаем курьера
        courierThread.start();
        Thread.sleep(4000); // Даем время на обработку заказа

        // Проверяем, что склад пуст (заказ был доставлен)
        assertTrue(warehouse.isEmpty(), "Склад должен быть пустым после доставки");

        // Завершаем работу курьера
        courierThread.interrupt();
        courierThread.join();
    }

    @Test
    void testCourierStopsOnInterrupt() throws InterruptedException {
        // Запускаем курьера, но не кладем заказы на склад
        courierThread.start();
        Thread.sleep(1000); // Даем время, чтобы он ждал заказ

        // Прерываем поток курьера
        courierThread.interrupt();
        courierThread.join();

        // Проверяем, что склад остался пустым (курьер не должен был взять заказ)
        assertTrue(warehouse.isEmpty(), "Склад должен оставаться пустым");
    }
}