package nsu.kochanov;

/**
 * Javadoc.
 */
class Baker implements Runnable {
    private final int id;
    private final OrderQueue queue;
    private final Warehouse warehouse;

    public Baker(int id, OrderQueue queue, Warehouse warehouse) {
        this.id = id;
        this.queue = queue;
        this.warehouse = warehouse;
    }

    /**
     * Javadoc.
     */
    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                Order order = queue.takeOrder();
                System.out.println("[" + order.getId() + "] Выпекает пекарь " + id);
                Thread.sleep(2000);
                warehouse.store(order);
            }
        } catch (InterruptedException e) {
            System.out.println("Пекарь " + id + " завершает работу.");
        }
    }
}