package com.cern.todo.Controller;

import com.cern.todo.DTOs.TaskUpdateDTO;
import com.cern.todo.Entities.Task;
import com.cern.todo.Entities.TaskCategory;
import com.cern.todo.Exception.ResourceNotFoundException;
import com.cern.todo.Repository.TaskCategoryRepository;
import com.cern.todo.Services.TaskService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/categories")
public class TaskCategoryController {

    private static final Logger logger = LoggerFactory.getLogger(TaskCategoryController.class);

    @Autowired
    private TaskCategoryRepository taskCategoryRepository;
    @Autowired
    private TaskService taskService;

    @GetMapping
    public List<TaskCategory> getAllCategories() {
        return taskCategoryRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<TaskCategory> createCategory(@RequestBody TaskCategory category) {
//        logger.info(category.toString());
        TaskCategory _category = taskCategoryRepository.save(category);
        return new ResponseEntity<>(_category, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskCategory> getCategoryById(@PathVariable Long id) {
        return taskCategoryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskCategory> updateCategory(@PathVariable Long id,
                                                       @RequestBody TaskCategory categoryDetails) {
        TaskCategory category = taskCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        category.setCategoryName(categoryDetails.getCategoryName());
        category.setCategoryDescription(categoryDetails.getCategoryDescription());
        final TaskCategory updatedCategory = taskCategoryRepository.save(category);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        TaskCategory category = taskCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        taskCategoryRepository.delete(category);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{categoryId}/tasks")
    public ResponseEntity<?> createTask(@PathVariable Long categoryId, @Valid @RequestBody TaskUpdateDTO taskRequest) {
        try {

            Task task = taskService.createTask(taskRequest, categoryId);

            return ResponseEntity.ok(task);

        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
