package nsu.kochanov;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class PizzeriaTest {
    private static final String TEST_ORDERS_FILE = "test_unfinished_orders.json";

    @BeforeEach
    void setUp() throws IOException {
        Files.deleteIfExists(new File(TEST_ORDERS_FILE).toPath());
        // Очищаем тестовый файл перед запуском
    }

    @Test
    void testLoadUnfinishedOrders() throws IOException {
        // Создаём JSON-файл с незавершёнными заказами
        String jsonContent = "[{\"id\":1}, {\"id\":2}, {\"id\":3}]";
        Files.write(new File(TEST_ORDERS_FILE).toPath(), jsonContent.getBytes());

        // Запускаем пиццерию — заказы должны загрузиться автоматически
        Pizzeria pizzeria = new Pizzeria(2, 2);

        // Добавляем новый заказ, чтобы убедиться, что система работает
        pizzeria.placeOrder(4);

        // Завершаем работу и проверяем, что все заказы (старые + новый) сохранились
        pizzeria.stop();

        // Читаем сохранённый файл
        String savedContent = Files.readString(new File(TEST_ORDERS_FILE).toPath());
        assertTrue(savedContent.contains("\"id\":1"), "Должен быть загружен заказ с ID 1");
        assertTrue(savedContent.contains("\"id\":2"), "Должен быть загружен заказ с ID 2");
        assertTrue(savedContent.contains("\"id\":3"), "Должен быть загружен заказ с ID 3");
        assertFalse(savedContent.contains("\"id\":4"), "Должен быть добавлен новый заказ с ID 4");
    }

    @Test
    void testPlaceOrderWhenClosed() {
        Pizzeria pizzeria = new Pizzeria(2, 2);
        pizzeria.stop();
        pizzeria.placeOrder(99);

        // Перезапускаем пиццерию и проверяем, что заказа 99 нет в JSON
        Pizzeria newPizzeria = new Pizzeria(2, 2);
        newPizzeria.stop();

        String savedContent;
        try {
            savedContent = Files.readString(new File(TEST_ORDERS_FILE).toPath());
        } catch (IOException e) {
            savedContent = "";
        }

        assertFalse(savedContent.contains("\"id\":99"), "После закрытия нельзя добавлять заказы");
    }
}