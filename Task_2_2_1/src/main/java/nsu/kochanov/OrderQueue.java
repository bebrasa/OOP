package nsu.kochanov;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Javadoc.
 */
class OrderQueue {
    private final Queue<Order> queue = new LinkedList<>();
    private volatile boolean acceptingOrders = true;

    /**
     * Javadoc.
     */
    public synchronized void addOrder(Order order) {
        if (!acceptingOrders) {
            System.out.println("[" + order.getId() + "] Заказ не может быть добавлен, "
                    + "так как пиццерия закрывается.");
            return;
        }
        queue.offer(order);
        notifyAll();
        System.out.println("[" + order.getId() + "] Добавлен в очередь");
    }

    /**
     * Javadoc.
     */
    public synchronized Order takeOrder() throws InterruptedException {
        while (queue.isEmpty() && acceptingOrders) {
            wait();
        }
        return queue.poll();
    }

    /**
     * Javadoc.
     */
    public synchronized void stopAcceptingOrders() {
        acceptingOrders = false;
        notifyAll();
    }

    /**
     * Javadoc.
     */
    public synchronized List<Order> getUnfinishedOrders() {
        return new LinkedList<>(queue);
    }
}