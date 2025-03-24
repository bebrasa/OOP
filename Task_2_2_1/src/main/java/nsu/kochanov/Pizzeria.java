package nsu.kochanov;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Javadoc.
 */
class Pizzeria {
    private final OrderQueue orderQueue;
    private final Warehouse warehouse;
    private final List<Thread> bakers;
    private final List<Thread> couriers;
    private volatile boolean isOpen = true;
    private static final String UNFINISHED_ORDERS_FILE = "unfinished_orders.json";


    public Pizzeria(int bakersCount, int couriersCount) {
        orderQueue = new OrderQueue();
        warehouse = new Warehouse();
        bakers = new LinkedList<>();
        couriers = new LinkedList<>();

        loadUnfinishedOrders();

        for (int i = 0; i < bakersCount; i++) {
            Thread baker = new Thread(new Baker(i + 1, orderQueue, warehouse));
            bakers.add(baker);
            baker.start();
        }

        for (int i = 0; i < couriersCount; i++) {
            Thread courier = new Thread(new Courier(i + 1, warehouse));
            couriers.add(courier);
            courier.start();
        }
    }

    /**
     * Javadoc.
     */
    public void placeOrder(int orderId) {
        if (!isOpen) {
            System.out.println("Заказ " + orderId + " не может быть принят, пиццерия закрыта.");
            return;
        }
        orderQueue.addOrder(new Order(orderId));
    }

    /**
     * Javadoc.
     */
    public synchronized void stop() {
        isOpen = false;
        System.out.println("Закрытие пиццерии...");

        orderQueue.stopAcceptingOrders();
        warehouse.stopProcessing();

        synchronized (orderQueue) {
            orderQueue.notifyAll();
        }
        synchronized (warehouse) {
            warehouse.notifyAll();
        }

        for (Thread baker : bakers) {
            try {
                baker.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        for (Thread courier : couriers) {
            try {
                courier.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        saveUnfinishedOrders();
        System.out.println("Пиццерия закрыта.");
    }

    /**
     * Javadoc.
     */
    private void saveUnfinishedOrders() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(UNFINISHED_ORDERS_FILE), orderQueue.getUnfinishedOrders());
            System.out.println("Незавершенные заказы сохранены.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Javadoc.
     */
    private void loadUnfinishedOrders() {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(UNFINISHED_ORDERS_FILE);
        if (file.exists()) {
            try {
                List<Order> unfinishedOrders = mapper.readValue(file,
                        mapper.getTypeFactory().constructCollectionType(List.class, Order.class));
                for (Order order : unfinishedOrders) {
                    orderQueue.addOrder(order);
                }
                System.out.println("Незавершенные заказы загружены.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
