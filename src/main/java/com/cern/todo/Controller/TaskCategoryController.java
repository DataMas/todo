package com.cern.todo.Controller;

import com.cern.todo.Entities.TaskCategory;
import com.cern.todo.Repository.TaskCategoryRepository;
import com.cern.todo.Services.TaskCategoryService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    private TaskCategoryService taskCategoryService;

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

//    @GetMapping("/names")
//    public ResponseEntity<List<String>> getAllCategoriesNames() {
//        List<String> categoryNames = taskCategoryService.getAllCategoryNames();
//        return new ResponseEntity<>(categoryNames, HttpStatus.OK);
//    }

    // Add more endpoints for update and delete if needed
}
