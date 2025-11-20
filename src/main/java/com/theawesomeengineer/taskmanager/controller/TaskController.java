package com.theawesomeengineer.taskmanager.controller;

import com.theawesomeengineer.taskmanager.api.TasksApi;
import com.theawesomeengineer.taskmanager.mapper.TaskMapper;
import com.theawesomeengineer.taskmanager.model.Task;
import com.theawesomeengineer.taskmanager.model.TaskRequest;
import com.theawesomeengineer.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class - handles HTTP requests for tasks
 * @RestController tells Spring this handles web requests
 * This implements the API interface generated from openapi.yaml
 */
@RestController
public class TaskController implements TasksApi {

    // Service handles business logic
    @Autowired
    private TaskService taskService;

    // Mapper converts between database objects and API objects
    @Autowired
    private TaskMapper taskMapper;

    // GET /tasks - get all tasks
    @Override
    public ResponseEntity<List<Task>> getAllTasks() {
        // Get tasks from database (these are entity objects)
        List<com.theawesomeengineer.taskmanager.entity.Task> taskEntities = taskService.getAllTasks();

        // Convert each entity to API model
        List<Task> apiTasks = new ArrayList<>();
        for (com.theawesomeengineer.taskmanager.entity.Task entity : taskEntities) {
            Task apiTask = taskMapper.toModel(entity);
            apiTasks.add(apiTask);
        }

        // Return with 200 OK status
        return ResponseEntity.ok(apiTasks);
    }

    // GET /tasks/{id} - get one task by ID
    @Override
    public ResponseEntity<Task> getTaskById(Long id) {
        // Get task from database
        com.theawesomeengineer.taskmanager.entity.Task taskEntity = taskService.getTaskById(id);

        // Convert to API model
        Task apiTask = taskMapper.toModel(taskEntity);

        // Return with 200 OK status
        return ResponseEntity.ok(apiTask);
    }

    // POST /tasks - create a new task
    @Override
    public ResponseEntity<Task> createTask(TaskRequest taskRequest) {
        // Get data from request
        String title = taskRequest.getTitle();
        String description = taskRequest.getDescription();
        Boolean completed = taskRequest.getCompleted();

        // Default completed to false if not provided
        if (completed == null) {
            completed = false;
        }

        // Create task in database
        com.theawesomeengineer.taskmanager.entity.Task createdEntity =
            taskService.createTask(title, description, completed);

        // Convert to API model
        Task apiTask = taskMapper.toModel(createdEntity);

        // Return with 201 CREATED status
        return ResponseEntity.status(HttpStatus.CREATED).body(apiTask);
    }

    // PUT /tasks/{id} - update an existing task
    @Override
    public ResponseEntity<Task> updateTask(Long id, TaskRequest taskRequest) {
        // Get data from request
        String title = taskRequest.getTitle();
        String description = taskRequest.getDescription();
        Boolean completed = taskRequest.getCompleted();

        // Default completed to false if not provided
        if (completed == null) {
            completed = false;
        }

        // Update task in database
        com.theawesomeengineer.taskmanager.entity.Task updatedEntity =
            taskService.updateTask(id, title, description, completed);

        // Convert to API model
        Task apiTask = taskMapper.toModel(updatedEntity);

        // Return with 200 OK status
        return ResponseEntity.ok(apiTask);
    }

    // DELETE /tasks/{id} - delete a task
    @Override
    public ResponseEntity<Void> deleteTask(Long id) {
        // Delete from database
        taskService.deleteTask(id);

        // Return 204 NO CONTENT status (success with no body)
        return ResponseEntity.noContent().build();
    }
}
