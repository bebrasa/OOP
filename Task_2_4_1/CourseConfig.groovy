import nsu.kochanov.task_2_4_1.model.*
import java.time.LocalDate

// Определение задач
course.task(new Task(
        id: "lab1",
        name: "Первая лаба",
        maxScore: 10,
        softDeadline: LocalDate.parse("2025-04-10"),
        hardDeadline: LocalDate.parse("2025-04-20")
))

course.task(new Task(
        id: "lab2",
        name: "Вторая лаба",
        maxScore: 15,
        softDeadline: LocalDate.parse("2025-04-25"),
        hardDeadline: LocalDate.parse("2025-05-05")
))

// Определение групп и студентов
def group1 = new Group(name: "Группа 23215")
group1.student(new Student(
        githubUsername: "bebrasa",
        fullName: "Кочанов Никита",
        repoUrl: "https://github.com/bebrasa/OOP"
))
group1.student(new Student(
        githubUsername: "kozlovwv",
        fullName: "Козлов Кирилл",
        repoUrl: "https://github.com/kozlovwv/OOP"
))
course.group(group1)

//def group2 = new Group(name: "Группа 23216")
//group2.student(new Student(
//        githubUsername: "student1",
//        fullName: "Иванов Иван",
//        repoUrl: "https://github.com/student1/OOP"
//))
//group2.student(new Student(
//        githubUsername: "student2",
//        fullName: "Петров Петр",
//        repoUrl: "https://github.com/student2/OOP"
//))
//course.group(group2)

// Определение контрольных точек
course.checkpoint(new Checkpoint(
        name: "Промежуточная 1",
        date: LocalDate.parse("2025-04-25")
))

course.checkpoint(new Checkpoint(
        name: "Промежуточная 2",
        date: LocalDate.parse("2025-05-15")
))
