package ch.taskmanager.model;

import ch.taskmanager.application.TaskManager;
import ch.taskmanager.model.Priority;

import java.util.Date;

/**
 * The Process class with unique unmodifiable
 * identifier (PID) and a priority (low, medium, high).
 * Lifecycle of a Process is handled by a Task Manager
 */
public class Process {

    private final String processIdentifier;

    private Priority priority;

    private Date createdAt;

    public enum SortBy {processIdentifier, priority, createdAt}

    public Process(Priority priority) {
        this.processIdentifier = "P" + TaskManager.idCounter++; // UUID.randomUUID() can also be used
        this.priority = priority;
        this.createdAt = new Date();
    }

    Process() {
        this(Priority.LOW);
    }

    public String getProcessIdentifier() {
        return processIdentifier;
    }

    public Priority getPriority() {
        return priority;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}


