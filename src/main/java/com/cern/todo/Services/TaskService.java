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

    /**
     * Get all tasks
     * @return List of all tasks
     */
    public List<TaskUpdateDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(this::convertToUpdateDTO).collect(Collectors.toList());
    }

    /**
     * Get tasks with status 0 or 1
     * @return List of relevant tasks
     */
    public List<TaskReadDTO> getShowTasks() {
        List<Integer> statuses = Arrays.asList(0, 1);
        List<Task> tasks = taskRepository.findByStatusIn(statuses);
        return tasks.stream().map(this::convertToReadDTO).collect(Collectors.toList());
    }

    /**
     * Updata task by ID
     * @param id The Id of the task to be updated
     * @param taskUpdateDTO The Information of the update
     * @return Updated task
     */
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

    /**
     * Create new task
     * @param taskUpdateDTO The information of the new task
     * @param categoryId The category ID of the new task
     * @return The created task
     */
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

    /**
     * Convert to UpdateDTO
     * @param task The task
     * @return TaskUpdateDTO
     */
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

    /**
     * Convert to ReadDTO
     * @param task The task
     * @return TaskReadDTO
     */
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
