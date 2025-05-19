package nsu.kochanov.task241.runner;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import nsu.kochanov.task241.compiler.JavaCompiler;
import nsu.kochanov.task241.model.Checkpoint;
import nsu.kochanov.task241.model.CourseConfig;
import nsu.kochanov.task241.model.Group;
import nsu.kochanov.task241.model.Student;
import nsu.kochanov.task241.model.Task;
import nsu.kochanov.task241.report.ReportGenerator;
import nsu.kochanov.task241.style.CodeStyleChecker;
import nsu.kochanov.task241.test.TestRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Тесты для класса TaskRunner.
 */
class TaskRunnerTest {

    @TempDir
    private File tempDir;
    
    private CourseConfig config;
    private TaskRunner taskRunner;

    /**
     * Создаёт конфигурацию для тестов перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        config = new CourseConfig();
        
        // Добавляем тестовую задачу
        Task task = new Task();
        task.id = "test1";
        task.name = "Test Task";
        task.maxScore = 10;
        task.softDeadline = LocalDate.now().minusDays(1);
        task.hardDeadline = LocalDate.now().plusDays(1);
        config.task(task);
        
        // Добавляем тестовую группу и студента
        Group group = new Group();
        group.name = "TestGroup";
        
        Student student = new Student();
        student.fullName = "Test Student";
        student.githubUsername = "testuser";
        student.repoUrl = "https://github.com/testuser/testrepo";
        group.student(student);
        
        config.group(group);
        
        // Создаем экземпляр TaskRunner
        taskRunner = new TaskRunner(config);
    }

    /**
     * Проверяет создание TaskRunner.
     */
    @Test
    void testTaskRunnerCreation() {
        assertNotNull(taskRunner);
    }

    /**
     * Проверяет методы добавления объектов в CourseConfig.
     */
    @Test
    void testCourseConfigAddObjects() {
        CourseConfig testConfig = new CourseConfig();
        
        // Добавляем объекты
        Task task = new Task();
        task.id = "test_task";
        testConfig.task(task);
        
        Group group = new Group();
        group.name = "test_group";
        testConfig.group(group);
        
        Checkpoint checkpoint = new Checkpoint();
        checkpoint.date = LocalDate.now();
        testConfig.checkpoint(checkpoint);
        
        // Проверяем, что объекты были добавлены
        assertEquals(1, testConfig.tasks.size());
        assertEquals("test_task", testConfig.tasks.get(0).id);
        
        assertEquals(1, testConfig.groups.size());
        assertEquals("test_group", testConfig.groups.get(0).name);
        
        assertEquals(1, testConfig.checkpoints.size());
        assertEquals(LocalDate.now(), testConfig.checkpoints.get(0).date);
    }
    
    /**
     * Проверяет создание и обновление задач в конфигурации.
     */
    @Test
    void testAddAndUpdateTaskInConfig() {
        
        // Добавляем задачу
        Task task = new Task();
        task.id = "task1";
        task.name = "Task 1";
        CourseConfig testConfig = new CourseConfig();
        task.maxScore = 5;
        testConfig.task(task);
        
        // Проверяем добавленную задачу
        assertEquals(1, testConfig.tasks.size());
        assertEquals("task1", testConfig.tasks.get(0).id);
        assertEquals("Task 1", testConfig.tasks.get(0).name);
        assertEquals(5, testConfig.tasks.get(0).maxScore);
        
        // Обновляем задачу
        testConfig.tasks.get(0).maxScore = 10;
        assertEquals(10, testConfig.tasks.get(0).maxScore);
    }
    
    /**
     * Проверяет добавление нескольких студентов в группу.
     */
    @Test
    void testAddMultipleStudentsToGroup() {
        Group group = new Group();
        group.name = "Test Group";
        
        // Создаем и добавляем студентов
        Student student1 = new Student();
        student1.fullName = "Student 1";
        student1.githubUsername = "student1";
        group.student(student1);
        
        Student student2 = new Student();
        student2.fullName = "Student 2";
        student2.githubUsername = "student2";
        group.student(student2);
        
        Student student3 = new Student();
        student3.fullName = "Student 3";
        student3.githubUsername = "student3";
        group.student(student3);
        
        // Проверяем, что все студенты добавлены
        assertEquals(3, group.students.size());
        assertEquals("Student 1", group.students.get(0).fullName);
        assertEquals("Student 2", group.students.get(1).fullName);
        assertEquals("Student 3", group.students.get(2).fullName);
    }
    
    /**
     * Проверяет создание временной директории для тестов.
     */
    @Test
    void testTempDirectoryCreation() {
        assertNotNull(tempDir);
        assertTrue(tempDir.exists());
        assertTrue(tempDir.isDirectory());
    }
    
    /**
     * Проверяет создание структуры проекта во временной директории.
     */
    @Test
    void testCreateProjectStructure() throws IOException {
        // Создаем корневую директорию проекта
        File projectDir = new File(tempDir, "testProject");
        projectDir.mkdirs();
        
        // Создаем build.gradle
        File gradleFile = new File(projectDir, "build.gradle");
        Files.writeString(gradleFile.toPath(), 
                "plugins {\n    id 'java'\n}\n\n"
                + "repositories {\n    mavenCentral()\n}");
        
        // Создаем структуру директорий
        File srcDir = new File(projectDir, "src/main/java");
        srcDir.mkdirs();
        
        File testDir = new File(projectDir, "src/test/java");
        testDir.mkdirs();
        
        // Проверяем созданную структуру
        assertTrue(projectDir.exists());
        assertTrue(gradleFile.exists());
        assertTrue(srcDir.exists());
        assertTrue(testDir.exists());
        
        // Проверяем содержимое build.gradle
        String gradleContent = Files.readString(gradleFile.toPath());
        assertTrue(gradleContent.contains("plugins"));
        assertTrue(gradleContent.contains("java"));
        assertTrue(gradleContent.contains("repositories"));
    }
}