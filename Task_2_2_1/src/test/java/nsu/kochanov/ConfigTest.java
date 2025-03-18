package nsu.kochanov;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import org.junit.jupiter.api.Test;

class ConfigTest {

    @Test
    void testLoadConfigSuccessfully() throws IOException {

        // Загружаем конфиг
        Config config = Config.loadConfig("pizzeria_config.json");

        // Проверяем, что конфиг не null и содержит ожидаемые значения
        assertNotNull(config, "Конфиг должен загрузиться");
        assertEquals(3, config.getNumBakers(), "Количество пекарей должно быть 3");
        assertEquals(1, config.getNumCouriers(), "Количество курьеров должно быть 1");

    }

    @Test
    void testLoadConfigFileNotFound() {
        Config config = Config.loadConfig("non_existing_file.json");
        assertNull(config, "Должен вернуться null при отсутствии файла");
    }
}