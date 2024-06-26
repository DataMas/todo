package com.cern.todo.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Timestamp;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "task_name", nullable = false,  length = 100)
    private String taskName;

    @Column(name = "task_description", length = 500)
    private String taskDescription;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "deadline")
    private Timestamp deadline;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private TaskCategory category;

    public Task() {

    }

    public Task(String taskName, String taskDescription, Timestamp deadline, TaskCategory category, Integer status) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.deadline = deadline;
        this.category = category;
        this.status = status;
    }


    public Long getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public Integer getStatus() {
        return status;
    }

    public Timestamp getDeadline() {
        return deadline;
    }

    public TaskCategory getCategory() {
        return category;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }

    public void setCategory(TaskCategory taskCategory) {
        this.category = taskCategory;
    }

}
