package nsu.kochanov;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;

class MainTest {

    @Test
    void testMainOutput() {
        // Перехватываем вывод в консоль
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // Запускаем main
        Main.main(new String[]{});

        // Возвращаем стандартный поток вывода
        System.setOut(originalOut);

        // Проверяем, что в выводе есть нужные результаты
        String output = outputStream.toString();
        assertTrue(output.contains("Последовательный (bR): true"));
        assertTrue(output.contains("Параллельный (Thread, 6 потока, bR): true"));
        assertTrue(output.contains("ParallelStream (bR): true"));
    }
}