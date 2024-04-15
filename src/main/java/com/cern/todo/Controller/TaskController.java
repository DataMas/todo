package com.cern.todo.Controller;

import com.cern.todo.DTOs.TaskReadDTO;
import com.cern.todo.DTOs.TaskUpdateDTO;
import com.cern.todo.Entities.Task;
import com.cern.todo.Exception.ResourceNotFoundException;
import com.cern.todo.Repository.TaskRepository;
import com.cern.todo.Services.TaskService;
import com.cern.todo.Utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskService taskService;

    /**
     * Get all tasks
     * @return List of all tasks
     */
    @GetMapping("/tasks")
    public ResponseEntity<List<TaskUpdateDTO>> getAllTasks() {
        List<TaskUpdateDTO> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    /**
     * Get task by ID
     * @param id The ID of the task to be deleted
     * @return Response with message
     */
    @GetMapping("/tasks/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id) {
     return taskRepository.findById(id)
             .map(ResponseEntity::ok)
             .orElseThrow(() -> new ResourceNotFoundException("Task with ID "+id+" not found"));
    }

    /**
     * Get tasks with status 0 or 1
     * @return List of tasks
     */
    @GetMapping("/tasks/statusShow")
    public ResponseEntity<List<TaskReadDTO>> getActiveTasks() {
        List<TaskReadDTO> tasks = taskService.getShowTasks();
        return ResponseEntity.ok(tasks);
    }

    /**
     * Update a task by ID
     * @param id The ID of the task to be updated
     * @param taskRequest The task information to be updated
     * @return Response with message
     */
    @PutMapping("/tasks/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody TaskUpdateDTO taskRequest) {
        Task updatedTask = taskService.updateTaskById(id, taskRequest);
        return ResponseEntity.ok(updatedTask);
    }

    /**
     * Delete a task by ID
     * @param id The ID of the task to be deleted
     * @return Response with message
     */
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with ID "+id+" not found"));
        taskRepository.delete(task);
        ApiResponse response = new ApiResponse("Task with id "+id+" deleted with success");
        return ResponseEntity.status(HttpStatus.GONE).body(response);
    }

    /**
     * Handle the api validation errors. Return a message with all the validation issues.
     * @param ex The exception
     * @return Response with error message
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {

        List<String> details = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        ApiResponse response = new ApiResponse("Validation Error: " + String.join(", ", details));
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Handle type mismatch validation exception. Return a message with all the validation issues.
     * @param ex The exception
     * @return Response with error message
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentTypeMismatchException ex) {
        ApiResponse response = new ApiResponse("Validation Error: " + ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Handle resource exception. Return a message with all the validation issues.
     * @param ex The exception
     * @return Response with error message
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceExceptions(ResourceNotFoundException ex) {
        ApiResponse response = new ApiResponse("Resource Error: " + ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}
