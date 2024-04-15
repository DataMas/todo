package com.cern.todo.DTOs;

import java.sql.Timestamp;

public class TaskReadDTO {
    private Long id;

    private String taskName;
    private String taskDescription;
    private String categoryName;
    private Timestamp deadline;
    private Integer status;

    public TaskReadDTO() {
    }

    public TaskReadDTO(String taskName, String taskDescription, String categoryName, Long categoryId, Timestamp deadline, Integer status) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.categoryName = categoryName;
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

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}