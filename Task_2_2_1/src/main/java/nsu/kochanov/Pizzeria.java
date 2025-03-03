package nsu.kochanov;

import java.util.ArrayList;
import java.util.List;

public class Pizzeria {
    private final Config config;
    private final OrderQueue orderQueue = new OrderQueue();
    private final Warehouse warehouse;
    private final List<Baker> bakers = new ArrayList<>();
    private final List<Courier> couriers = new ArrayList<>();
    private final List<Thread> bakerThreads = new ArrayList<>();
    private final List<Thread> courierThreads = new ArrayList<>();

    public Pizzeria(Config config) {
        this.config = config;
        this.warehouse = new Warehouse(config.getWarehouseCapacity());
        initializeBakers();
        initializeCouriers();
    }

    private void initializeBakers() {
        for (int i = 0; i < config.getNumBakers(); i++) {
            Baker baker = new Baker(i + 1, config.getBakerSpeeds()[i], orderQueue, warehouse);
            bakers.add(baker);
        }
    }

    private void initializeCouriers() {
        for (int i = 0; i < config.getNumCouriers(); i++) {
            Courier courier = new Courier(i + 1, config.getCourierCapacities()[i], warehouse);
            couriers.add(courier);
        }
    }

    public void start() {
        // Starting baker threads
        for (Baker baker : bakers) {
            Thread bakerThread = new Thread(baker);
            bakerThreads.add(bakerThread);
            bakerThread.start();
        }

        // Starting courier threads
        for (Courier courier : couriers) {
            Thread courierThread = new Thread(courier);
            courierThreads.add(courierThread);
            courierThread.start();
        }
    }

    public void stop() {
        // Stopping acceptance of new orders
        orderQueue.stopAcceptingOrders();

        // Interrupting baker threads
        for (Thread bakerThread : bakerThreads) {
            bakerThread.interrupt();
        }

        // Interrupting courier threads
        for (Thread courierThread : courierThreads) {
            courierThread.interrupt();
        }

        // Waiting for baker threads to finish
        for (Thread bakerThread : bakerThreads) {
            try {
                bakerThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Waiting for courier threads to finish
        for (Thread courierThread : courierThreads) {
            try {
                courierThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void placeOrder(Order order) {
        orderQueue.addOrder(order);
    }
}