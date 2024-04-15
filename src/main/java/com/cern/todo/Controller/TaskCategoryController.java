package com.cern.todo.Controller;

import com.cern.todo.DTOs.TaskUpdateDTO;
import com.cern.todo.Entities.Task;
import com.cern.todo.Entities.TaskCategory;
import com.cern.todo.Exception.ResourceNotFoundException;
import com.cern.todo.Repository.TaskCategoryRepository;
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
@RequestMapping("/api/categories")
public class TaskCategoryController {

    @Autowired
    private TaskCategoryRepository taskCategoryRepository;
    @Autowired
    private TaskService taskService;

    /**
     * Read all the categories
     * @return Response with message
     */
    @GetMapping
    public List<TaskCategory> getAllCategories() {
        return taskCategoryRepository.findAll();
    }

    /**
     * Create a new category
     * @param category The request body with the category info
     * @return Response with message
     */
    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody TaskCategory category) {
        TaskCategory _category = taskCategoryRepository.save(category);
        return new ResponseEntity<>(_category, HttpStatus.CREATED);
    }

    /**
     * Read a category by ID
     * @param id The ID of the category to be read
     * @return Response with message
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskCategory> getCategoryById(@PathVariable Long id) {
        return taskCategoryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update a category by ID
     * @param id The ID of the category to be updated
     * @param categoryDetails The request body containing the updated category info
     * @return Response with message
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskCategory> updateCategory(@PathVariable Long id,
                                                       @Valid @RequestBody TaskCategory categoryDetails) {
        TaskCategory category = taskCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        category.setCategoryName(categoryDetails.getCategoryName());
        category.setCategoryDescription(categoryDetails.getCategoryDescription());
        final TaskCategory updatedCategory = taskCategoryRepository.save(category);
        return ResponseEntity.ok(updatedCategory);
    }

    /**
     * Delete category by ID
     * @param id The ID of the category to be deleted
     * @return Response with message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        TaskCategory category = taskCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        taskCategoryRepository.delete(category);
        ApiResponse response = new ApiResponse("Category with id " + id + " deleted with success");
        return ResponseEntity.status(HttpStatus.GONE).body(response);
    }

    /**
     * Create an new task. Assign the given category to the task by reference.
     * @param categoryId The ID of the category
     * @param taskRequest The body of the request
     * @return Reposnse with message
     */
    @PostMapping("/{categoryId}/tasks")
    public ResponseEntity<?> createTask(@PathVariable Long categoryId, @Valid @RequestBody TaskUpdateDTO taskRequest) {
        try {
            Task task = taskService.createTask(taskRequest, categoryId);

            return ResponseEntity.ok(task);

        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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
