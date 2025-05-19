package nsu.kochanov.task241.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

class TaskTest {
    
    @Test
    void testCreateTask() {
        Task task = new Task();
        assertNotNull(task);
    }
    
    @Test
    void testSetTaskFields() {
        Task task = new Task();
        
        task.id = "task1";
        task.name = "Задание 1";
        task.maxScore = 100;
        task.softDeadline = LocalDate.of(2023, 10, 15);
        task.hardDeadline = LocalDate.of(2023, 10, 30);
        
        assertEquals("task1", task.id);
        assertEquals("Задание 1", task.name);
        assertEquals(100, task.maxScore);
        assertEquals(LocalDate.of(2023, 10, 15), task.softDeadline);
        assertEquals(LocalDate.of(2023, 10, 30), task.hardDeadline);
    }
    
    @Test
    void testTaskFieldsInitiallyNull() {
        Task task = new Task();
        
        assertNull(task.id);
        assertNull(task.name);
        assertEquals(0, task.maxScore);
        assertNull(task.softDeadline);
        assertNull(task.hardDeadline);
    }
}