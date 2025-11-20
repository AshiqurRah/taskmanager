package com.theawesomeengineer.taskmanager.repository;

import com.theawesomeengineer.taskmanager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository - talks to the database
 * JpaRepository gives us free methods like save(), findAll(), etc.
 * We don't need to write SQL - Spring does it for us!
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // This interface is empty, but we get these methods automatically:
    // - save(task) - saves or updates a task
    // - findById(id) - finds a task by ID
    // - findAll() - gets all tasks
    // - deleteById(id) - deletes a task
    // - existsById(id) - checks if task exists

    // That's the power of Spring Data JPA - no code needed!
}
