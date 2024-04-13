package com.cern.todo.Services;

import com.cern.todo.Entities.TaskCategory;
import com.cern.todo.Repository.TaskCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskCategoryService {

    private final TaskCategoryRepository repository;
    private final TaskCategoryRepository taskCategoryRepository;

    @Autowired
    public TaskCategoryService(TaskCategoryRepository repository, TaskCategoryRepository taskCategoryRepository) {
        this.repository = repository;
        this.taskCategoryRepository = taskCategoryRepository;
    }

    public List<String> getAllCategoryNames() {
        return taskCategoryRepository.findAll().stream()
                .map(TaskCategory::getCategoryName)
                .collect(Collectors.toList());
    }
}
