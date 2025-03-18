package nsu.kochanov;

/**
 * Javadoc.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Config config = Config.loadConfig("pizzeria_config.json");
        if (config == null) {
            System.out.println("Ошибка загрузки конфигурации.");
            return;
        }

        Pizzeria pizzeria = new Pizzeria(config.getNumBakers(), config.getNumCouriers());

        for (int i = 1; i <= 20; i++) {
            pizzeria.placeOrder(i);
            Thread.sleep(1500);
        }

        Thread.sleep(1000);
        pizzeria.stop();
    }
}