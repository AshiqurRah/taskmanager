package com.theawesomeengineer.taskmanager.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Task Entity - This represents a task in the database
 * @Entity tells Spring this is a database table
 * Each field below becomes a column in the database
 */
@Entity
@Table(name = "tasks")
public class Task {

    // Primary key - auto-generated ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Task title - cannot be null
    @Column(nullable = false)
    private String title;

    // Task description - cannot be null
    @Column(nullable = false)
    private String description;

    // Is the task completed? Default is false
    @Column(nullable = false)
    private Boolean completed = false;

    // When was this task created?
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // When was this task last updated?
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // This runs automatically before saving a new task
    @PrePersist
    public void beforeSave() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    // This runs automatically before updating an existing task
    @PreUpdate
    public void beforeUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Empty constructor - required by JPA
    public Task() {
    }

    // Constructor to create a new task easily
    public Task(String title, String description, Boolean completed) {
        this.title = title;
        this.description = description;
        if (completed != null) {
            this.completed = completed;
        } else {
            this.completed = false;
        }
    }

    // Getters and Setters - these let us read and write the fields

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
