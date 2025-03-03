package nsu.kochanov;

import java.io.InputStream;
import java.io.Reader;
import com.google.gson.Gson;
import java.io.InputStreamReader;

public class Config {
    private int numBakers;
    private int numCouriers;
    private int warehouseCapacity;
    private int[] bakerSpeeds;
    private int[] courierCapacities;

    public static Config loadConfig(String fileName) {
        Gson gson = new Gson();
        ClassLoader classLoader = Config.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(fileName)) {
            if (inputStream == null) {
                System.err.println("Файл не найден в classpath: " + fileName);
                return null;
            }
            try (Reader reader = new InputStreamReader(inputStream)) {
                return gson.fromJson(reader, Config.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Геттеры для полей
    public int getNumBakers() { return numBakers; }
    public int getNumCouriers() { return numCouriers; }
    public int getWarehouseCapacity() { return warehouseCapacity; }
    public int[] getBakerSpeeds() { return bakerSpeeds; }
    public int[] getCourierCapacities() { return courierCapacities; }
}