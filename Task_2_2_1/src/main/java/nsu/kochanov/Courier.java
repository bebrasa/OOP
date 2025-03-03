package nsu.kochanov;

public class Courier implements Runnable {
    private final int id;
    private final int capacity;
    private final Warehouse warehouse;
    private boolean running = true;

    public Courier(int id, int capacity, Warehouse warehouse) {
        this.id = id;
        this.capacity = capacity;
        this.warehouse = warehouse;
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Order[] orders = warehouse.retrievePizzas(capacity);
                if (orders.length == 0) {
                    break;
                }
                for (Order order : orders) {
                    System.out.println("Заказ " + order.getOrderId() + " доставляется курьером " + id);
                }
                Thread.sleep(5000); // Время доставки
                for (Order order : orders) {
                    System.out.println("Заказ " + order.getOrderId() + " доставлен курьером " + id);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}