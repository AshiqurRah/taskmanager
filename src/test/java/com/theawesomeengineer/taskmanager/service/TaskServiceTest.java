package com.theawesomeengineer.taskmanager.service;

import com.theawesomeengineer.taskmanager.entity.Task;
import com.theawesomeengineer.taskmanager.exception.TaskNotFoundException;
import com.theawesomeengineer.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TaskService.
 * Uses Mockito to mock the TaskRepository.
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task testTask;

    @BeforeEach
    void setUp() {
        testTask = new Task("Test Task", "Test Description", false);
        testTask.setId(1L);
    }

    @Test
    void getAllTasks_ShouldReturnAllTasks() {
        // Arrange
        Task task2 = new Task("Task 2", "Description 2", true);
        task2.setId(2L);
        List<Task> expectedTasks = Arrays.asList(testTask, task2);
        when(taskRepository.findAll()).thenReturn(expectedTasks);

        // Act
        List<Task> actualTasks = taskService.getAllTasks();

        // Assert
        assertEquals(2, actualTasks.size());
        assertEquals(expectedTasks, actualTasks);
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void getTaskById_WithValidId_ShouldReturnTask() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));

        // Act
        Task actualTask = taskService.getTaskById(1L);

        // Assert
        assertNotNull(actualTask);
        assertEquals(testTask.getId(), actualTask.getId());
        assertEquals(testTask.getTitle(), actualTask.getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void getTaskById_WithInvalidId_ShouldThrowException() {
        // Arrange
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        TaskNotFoundException exception = assertThrows(
            TaskNotFoundException.class,
            () -> taskService.getTaskById(999L)
        );
        assertTrue(exception.getMessage().contains("999"));
        verify(taskRepository, times(1)).findById(999L);
    }

    @Test
    void createTask_ShouldSaveAndReturnTask() {
        // Arrange
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        // Act
        Task createdTask = taskService.createTask("Test Task", "Test Description", false);

        // Assert
        assertNotNull(createdTask);
        assertEquals("Test Task", createdTask.getTitle());
        assertEquals("Test Description", createdTask.getDescription());
        assertFalse(createdTask.getCompleted());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void updateTask_WithValidId_ShouldUpdateAndReturnTask() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        // Act
        Task updatedTask = taskService.updateTask(1L, "Updated Title", "Updated Description", true);

        // Assert
        assertNotNull(updatedTask);
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void updateTask_WithInvalidId_ShouldThrowException() {
        // Arrange
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
            TaskNotFoundException.class,
            () -> taskService.updateTask(999L, "Title", "Description", false)
        );
        verify(taskRepository, times(1)).findById(999L);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void deleteTask_WithValidId_ShouldDeleteTask() {
        // Arrange
        when(taskRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(1L);

        // Act
        taskService.deleteTask(1L);

        // Assert
        verify(taskRepository, times(1)).existsById(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTask_WithInvalidId_ShouldThrowException() {
        // Arrange
        when(taskRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        TaskNotFoundException exception = assertThrows(
            TaskNotFoundException.class,
            () -> taskService.deleteTask(999L)
        );
        assertTrue(exception.getMessage().contains("999"));
        verify(taskRepository, times(1)).existsById(999L);
        verify(taskRepository, never()).deleteById(999L);
    }
}
