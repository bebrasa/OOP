package nsu.kochanov;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Javadoc.
 */
class Warehouse {
    private final Queue<Order> storage = new LinkedList<>();
    private static final int CAPACITY = 5;
    private volatile boolean processing = true;

    /**
     * Javadoc.
     */
    public synchronized void store(Order order) throws InterruptedException {
        while (storage.size() >= CAPACITY && processing) {
            wait();
        }
        if (!processing) {
            return;
        }
        storage.offer(order);
        notifyAll();
        System.out.println("[" + order.getId() + "] Готова к доставке");
    }

    /**
     * Javadoc.
     */
    public synchronized Order take() throws InterruptedException {
        while (storage.isEmpty() && processing) {
            wait();
        }
        if (!processing) {
            return null;
        }
        Order order = storage.poll();
        notifyAll();
        return order;
    }

    /**
     * Javadoc.
     */
    public synchronized void stopProcessing() {
        processing = false;
        notifyAll();
    }

    /**
     * Javadoc.
     */
    public synchronized boolean isEmpty() {
        return storage.isEmpty();
    }
}
