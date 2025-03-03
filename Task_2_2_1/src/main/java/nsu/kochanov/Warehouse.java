package nsu.kochanov;

import java.util.LinkedList;
import java.util.Queue;

public class Warehouse {
    private final int capacity;
    private final Queue<Order> storage = new LinkedList<>();

    public Warehouse(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void storePizza(Order order) throws InterruptedException {
        while (storage.size() >= capacity) {
            wait();
        }
        storage.add(order);
        System.out.println("Заказ " + order.getOrderId() + " помещен на склад");
        notifyAll();
    }

    public synchronized Order[] retrievePizzas(int maxCount) throws InterruptedException {
        while (storage.isEmpty()) {
            wait();
        }
        int count = Math.min(maxCount, storage.size());
        Order[] orders = new Order[count];
        for (int i = 0; i < count; i++) {
            orders[i] = storage.poll();
            System.out.println("Заказ " + orders[i].getOrderId() + " забран со склада");
        }
        notifyAll();
        return orders;
    }
}