package nsu.kochanov.task241.model;

import java.time.LocalDate;

/**
 * Класс, представляющий задачу курса.
 */
public class Task {
    /** Идентификатор задачи. */
    public String id;
    
    /** Название задачи. */
    public String name;
    
    /** Максимальный балл за задачу. */
    public int maxScore;
    
    /** Дата мягкого дедлайна (после которой применяется штраф). */
    public LocalDate softDeadline;
    
    /** Дата жесткого дедлайна (после которой применяется больший штраф). */
    public LocalDate hardDeadline;
}
