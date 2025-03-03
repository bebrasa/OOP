package nsu.kochanov;

public class Order {
    private static int idCounter = 0;
    private final int orderId;

    public Order() {
        this.orderId = ++idCounter;
    }

    public int getOrderId() {
        return orderId;
    }
}