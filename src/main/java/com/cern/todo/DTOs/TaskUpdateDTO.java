package com.cern.todo.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;

public class TaskUpdateDTO {
    private Long id;
    @NotNull(message = "Task name cannot be null")
    @NotBlank(message = "Task name cannot be blank")
    private String taskName;
    private String taskDescription;
    private String categoryName;
    private Long categoryId;
    @NotNull(message = "Task deadline cannot be null")
    private Timestamp deadline;
    @NotNull(message = "Task name cannot be null")
    private Integer status;

    public TaskUpdateDTO() {
    }

    public TaskUpdateDTO(String taskName, String taskDescription, String categoryName, Long categoryId, Timestamp deadline, Integer status) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.categoryName = categoryName;
        this.categoryId = categoryId;
        this.deadline = deadline;
        this.status = status;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public Timestamp getDeadline() {
        return deadline;
    }

    public Integer getStatus() {
        return status;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}