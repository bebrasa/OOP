package nsu.kochanov;

/**
 * Javadoc.
 */
class Courier implements Runnable {
    private final int id;
    private final Warehouse warehouse;

    public Courier(int id, Warehouse warehouse) {
        this.id = id;
        this.warehouse = warehouse;
    }

    /**
     * Javadoc.
     */
    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                Order order = warehouse.take();
                System.out.println("[" + order.getId() + "] Доставляет курьер " + id);
                Thread.sleep(3000);
                System.out.println("[" + order.getId() + "] Доставлена");
            }
        } catch (InterruptedException e) {
            System.out.println("Курьер " + id + " завершает работу.");
        }
    }
}
