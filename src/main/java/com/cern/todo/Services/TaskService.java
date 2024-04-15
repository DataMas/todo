package com.cern.todo.Services;

import com.cern.todo.DTOs.TaskUpdateDTO;
import com.cern.todo.DTOs.TaskReadDTO;
import com.cern.todo.Entities.Task;
import com.cern.todo.Entities.TaskCategory;
import com.cern.todo.Exception.ResourceNotFoundException;
import com.cern.todo.Repository.TaskCategoryRepository;
import com.cern.todo.Repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskCategoryRepository taskCategoryRepository;

    public TaskService(TaskRepository taskRepository, TaskCategoryRepository taskCategoryRepository) {
        this.taskRepository = taskRepository;
        this.taskCategoryRepository = taskCategoryRepository;
    }

    public List<TaskUpdateDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(this::convertToUpdateDTO).collect(Collectors.toList());
    }

    public List<TaskReadDTO> getShowTasks() {
        List<Integer> statuses = Arrays.asList(0, 1);
        List<Task> tasks = taskRepository.findByStatusIn(statuses);
        return tasks.stream().map(this::convertToReadDTO).collect(Collectors.toList());
    }

    public Task updateTaskById(Long id, TaskUpdateDTO taskUpdateDTO) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with ID "+id+" not found"));

        if(taskUpdateDTO.getCategoryId() != null) {
            TaskCategory category = taskCategoryRepository.findById(taskUpdateDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category with "+ taskUpdateDTO.getCategoryId()+ " not found"));

            task.setCategory(category);
        }


        task.setTaskName(taskUpdateDTO.getTaskName());
        task.setTaskDescription(taskUpdateDTO.getTaskDescription());
        task.setDeadline(taskUpdateDTO.getDeadline());
        task.setStatus(taskUpdateDTO.getStatus());

        final Task updatedTask = taskRepository.save(task);

        return updatedTask;
    }

    public Task createTask(TaskUpdateDTO taskUpdateDTO, Long categoryId) {
        TaskCategory category = taskCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

        Task task = new Task();
        task.setTaskName(taskUpdateDTO.getTaskName());
        task.setTaskDescription(taskUpdateDTO.getTaskDescription());
        task.setDeadline(taskUpdateDTO.getDeadline());
        task.setStatus(taskUpdateDTO.getStatus());
        task.setCategory(category);

        return taskRepository.save(task);
    }

    private TaskUpdateDTO convertToUpdateDTO(Task task) {
        TaskUpdateDTO taskDTO = new TaskUpdateDTO();

        taskDTO.setId(task.getTaskId());
        taskDTO.setTaskName(task.getTaskName());
        taskDTO.setTaskDescription(task.getTaskDescription());
        taskDTO.setCategoryName(task.getCategory().getCategoryName());
        taskDTO.setCategoryId(task.getCategory().getCategoryId());
        taskDTO.setDeadline(task.getDeadline());
        taskDTO.setStatus(task.getStatus());
        return taskDTO;

    }

    private TaskReadDTO convertToReadDTO(Task task) {
        TaskReadDTO taskReadDTO = new TaskReadDTO();

        taskReadDTO.setId(task.getTaskId());
        taskReadDTO.setTaskName(task.getTaskName());
        taskReadDTO.setTaskDescription(task.getTaskDescription());
        taskReadDTO.setCategoryName(task.getCategory().getCategoryName());
        taskReadDTO.setDeadline(task.getDeadline());
        taskReadDTO.setStatus(task.getStatus());
        return taskReadDTO;

    }
}
