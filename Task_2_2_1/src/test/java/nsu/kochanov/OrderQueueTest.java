package nsu.kochanov;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderQueueTest {
    private OrderQueue orderQueue;

    @BeforeEach
    void setUp() {
        orderQueue = new OrderQueue();
    }

    @Test
    void testAddOrder() {
        Order order = new Order(1);
        orderQueue.addOrder(order);

        List<Order> unfinishedOrders = orderQueue.getUnfinishedOrders();
        assertEquals(1, unfinishedOrders.size(), "В очереди должен быть один заказ");
        assertEquals(1, unfinishedOrders.get(0).getId(), "ID заказа должен совпадать");
    }

    @Test
    void testTakeOrder() throws InterruptedException {
        Order order = new Order(2);
        orderQueue.addOrder(order);

        Order takenOrder = orderQueue.takeOrder();
        assertEquals(2, takenOrder.getId(), "ID взятого заказа должен совпадать");
    }

    @Test
    void testGetUnfinishedOrders() {
        orderQueue.addOrder(new Order(3));
        orderQueue.addOrder(new Order(4));

        List<Order> unfinishedOrders = orderQueue.getUnfinishedOrders();
        assertEquals(2, unfinishedOrders.size(), "Должно быть 2 незавершенных заказа");
        assertEquals(3, unfinishedOrders.get(0).getId(), "Первый заказ должен быть с ID 3");
        assertEquals(4, unfinishedOrders.get(1).getId(), "Второй заказ должен быть с ID 4");
    }

    @Test
    void testQueueWaitsForOrder() throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                orderQueue.takeOrder();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        thread.start();
        Thread.sleep(500); // Даем потоку уйти в ожидание

        assertTrue(thread.isAlive(), "Поток должен ожидать заказов");

        // Добавляем заказ, поток должен продолжить выполнение
        orderQueue.addOrder(new Order(5));
        thread.join(1000); // Ждём, пока поток обработает заказ

        assertFalse(thread.isAlive(), "Поток должен завершиться после получения заказа");
    }
}