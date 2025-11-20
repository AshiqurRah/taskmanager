package com.theawesomeengineer.taskmanager.service;

import com.theawesomeengineer.taskmanager.entity.Task;
import com.theawesomeengineer.taskmanager.exception.TaskNotFoundException;
import com.theawesomeengineer.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class - handles the business logic for tasks
 * This sits between the controller (API) and the repository (database)
 */
@Service
public class TaskService {

    // This connects to the database
    @Autowired
    private TaskRepository taskRepository;

    // Get all tasks from database
    public List<Task> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks;
    }

    // Get one task by its ID
    public Task getTaskById(Long id) {
        // Try to find the task
        Optional<Task> taskOptional = taskRepository.findById(id);

        // If task exists, return it. Otherwise throw error
        if (taskOptional.isPresent()) {
            return taskOptional.get();
        } else {
            throw new TaskNotFoundException("Task with ID " + id + " not found");
        }
    }

    // Create a new task
    public Task createTask(String title, String description, Boolean completed) {
        // Create new task object
        Task newTask = new Task();
        newTask.setTitle(title);
        newTask.setDescription(description);

        // Set completed, default to false if null
        if (completed != null) {
            newTask.setCompleted(completed);
        } else {
            newTask.setCompleted(false);
        }

        // Save to database and return
        Task savedTask = taskRepository.save(newTask);
        return savedTask;
    }

    // Update an existing task
    public Task updateTask(Long id, String title, String description, Boolean completed) {
        // First, find the existing task
        Task existingTask = getTaskById(id);

        // Update the fields
        existingTask.setTitle(title);
        existingTask.setDescription(description);
        existingTask.setCompleted(completed);

        // Save updated task to database
        Task updatedTask = taskRepository.save(existingTask);
        return updatedTask;
    }

    // Delete a task
    public void deleteTask(Long id) {
        // Check if task exists first
        boolean exists = taskRepository.existsById(id);

        if (!exists) {
            throw new TaskNotFoundException("Task with ID " + id + " not found");
        }

        // Delete the task
        taskRepository.deleteById(id);
    }
}
