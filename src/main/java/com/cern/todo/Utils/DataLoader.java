package com.cern.todo.Utils;

import com.cern.todo.Entities.TaskCategory;
import com.cern.todo.Repository.TaskCategoryRepository;
import com.cern.todo.Repository.TaskRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {
    @Bean
    CommandLineRunner loadData(TaskCategoryRepository taskCategoryRepository, TaskRepository taskRepository) {
        return args -> {
            taskCategoryRepository.save(new TaskCategory("Category 1", "This is the testing category 1. It is used for testing and is made automatically on start."));
            taskCategoryRepository.save(new TaskCategory("Category 2", "This is the testing category 2. It is used for testing and is made automatically on start."));

//            taskRepository.save(new TaskRepository("Category 1", "This is the testing category 1. It is used for testing and is made automatically on start."));
        };
    }
}
