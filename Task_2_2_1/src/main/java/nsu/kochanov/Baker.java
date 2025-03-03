package nsu.kochanov;

public class Baker implements Runnable {
    private final int id;
    private final int speed;
    private final OrderQueue orderQueue;
    private final Warehouse warehouse;
    private boolean running = true;

    public Baker(int id, int speed, OrderQueue orderQueue, Warehouse warehouse) {
        this.id = id;
        this.speed = speed;
        this.orderQueue = orderQueue;
        this.warehouse = warehouse;
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Order order = orderQueue.takeOrder();
                if (order == null) {
                    break;
                }
                System.out.println("Заказ " + order.getOrderId() + " готовится пекарем " + id);
                Thread.sleep(speed * 1000);
                System.out.println("Заказ " + order.getOrderId() + " готов пекарем " + id);
                warehouse.storePizza(order);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}