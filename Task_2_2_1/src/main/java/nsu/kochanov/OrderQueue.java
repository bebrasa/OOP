package nsu.kochanov;

import java.util.LinkedList;
import java.util.Queue;

public class OrderQueue {
    private final Queue<Order> queue = new LinkedList<>();
    private boolean acceptingOrders = true;

    public synchronized void addOrder(Order order) {
        if (!acceptingOrders) {
            System.out.println("Заказ " + order.getOrderId() + " не может быть добавлен: прием заказов остановлен");
            return;
        }
        queue.add(order);
        System.out.println("Заказ " + order.getOrderId() + " добавлен в очередь");
        notifyAll();
    }

    public synchronized Order takeOrder() throws InterruptedException {
        while (queue.isEmpty() && acceptingOrders) {
            wait();
        }
        if (queue.isEmpty() && !acceptingOrders) {
            return null; // Возвращаем null, если очередь пуста и прием заказов остановлен
        }
        Order order = queue.poll();
        System.out.println("Заказ " + order.getOrderId() + " взят в обработку");
        return order;
    }

    public synchronized void stopAcceptingOrders() {
        acceptingOrders = false;
        System.out.println("Прием новых заказов остановлен");
        notifyAll();
    }
}