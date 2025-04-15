import nsu.kochanov.model.*
import java.time.LocalDate

course.task(new Task(
        id: "lab1",
        name: "Первая лаба",
        maxScore: 10,
        softDeadline: LocalDate.parse("2025-04-10"),
        hardDeadline: LocalDate.parse("2025-04-20")
))

def group = new Group(name: "Группа 1")
group.student(new Student(
        githubUsername: "bebrasa",
        fullName: "Кочанов Никита",
        repoUrl: "https://github.com/bebrasa/OOP"
))
group.student(new Student(
        githubUsername: "kozlovwv",
        fullName: "Козлов Кирилл",
        repoUrl: "https://github.com/kozlovwv/OOP"
))
course.group(group)

course.checkpoint(new Checkpoint(
        name: "Промежуточная 1",
        date: LocalDate.parse("2025-04-25")
))