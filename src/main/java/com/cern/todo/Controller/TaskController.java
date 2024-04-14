package com.cern.todo.Controller;

import com.cern.todo.Entities.Task;
import com.cern.todo.Exception.ResourceNotFoundException;
import com.cern.todo.Repository.TaskCategoryRepository;
import com.cern.todo.Repository.TaskRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskCategoryRepository taskCategoryRepository;

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskRepository.findAll());
    }

    @PostMapping("/categories/{categoryId}/tasks")
    public ResponseEntity<Task> createTask(@PathVariable Long categoryId, @RequestBody Task taskRequest) {
        Task newTask = taskCategoryRepository.findById(categoryId).map(category -> {
            taskRequest.setCategory(category);
            return taskRepository.save(taskRequest);
        }).orElseThrow(() -> new ResourceNotFoundException("Category with ID "+categoryId+" not found"));

        return ResponseEntity.ok(newTask);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long taskId) {
     Task _task = taskRepository.findById(taskId)
             .orElseThrow(() -> new ResourceNotFoundException("Task with ID "+taskId+" not found"));
     return ResponseEntity.ok(_task);
    }

    //TODO Check later
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task taskRequest) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with ID "+id+" not found"));

        task.setTaskName(taskRequest.getTaskName());
        task.setTaskDescription(taskRequest.getTaskDescription());
        task.setDeadline(taskRequest.getDeadline());

        final Task updatedTask = taskRepository.save(task);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task with ID "+taskId+" not found"));

        taskRepository.delete(task);
        return ResponseEntity.noContent().build();
    }
}
