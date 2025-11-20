package com.theawesomeengineer.taskmanager.exception;

/**
 * Custom exception thrown when a task is not found in the database.
 * This will be caught by the global exception handler.
 */
public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(String message) {
        super(message);
    }

    public TaskNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
