package com.cern.todo;

import com.cern.todo.Controller.TaskCategoryController;
import com.cern.todo.DTOs.TaskUpdateDTO;
import com.cern.todo.Entities.Task;
import com.cern.todo.Entities.TaskCategory;
import com.cern.todo.Repository.TaskCategoryRepository;
import com.cern.todo.Services.TaskService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class TodoApplicationTests {

	@Mock
	private TaskCategoryRepository taskCategoryRepository;
	@InjectMocks
	private TaskCategoryController taskCategoryController;
	@Mock
	private TaskService taskService;

	private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


	@Test
	void contextLoads() {
	}

	@Test
	public void testGetAllCategories() {

		List<TaskCategory> categories = new ArrayList<>();
		categories.add(new TaskCategory());
		when(taskCategoryRepository.findAll()).thenReturn(categories);

		List<TaskCategory> result = taskCategoryController.getAllCategories();

		assertEquals(categories.size(), result.size());
	}

	@Test
	public void testCreateCategory() {
		// Mocking
		TaskCategory category = new TaskCategory();
		when(taskCategoryRepository.save(any(TaskCategory.class))).thenReturn(category);

		// Test
		ResponseEntity<?> response = taskCategoryController.createCategory(new TaskCategory());

		// Assertion
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertFalse(response.getBody() == null);
	}

	@Test
	public void testGetCategoryById_ExistingId() {
		// Mocking
		Long id = 1L;
		TaskCategory category = new TaskCategory();
		category.setCategoryId(id);
		when(taskCategoryRepository.findById(id)).thenReturn(Optional.of(category));

		// Test
		ResponseEntity<TaskCategory> response = taskCategoryController.getCategoryById(id);

		// Assertion
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertFalse(response.getBody() == null);
	}

	@Test
	public void testUpdateCategory() {
		Long id = 1L;
		TaskCategory category = new TaskCategory();
		category.setCategoryId(id);
		when(taskCategoryRepository.findById(id)).thenReturn(Optional.of(category));
		when(taskCategoryRepository.save(any(TaskCategory.class))).thenReturn(category);

		ResponseEntity<?> response = taskCategoryController.updateCategory(id, new TaskCategory());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertFalse(response.getBody() == null);
	}

	@Test
	public void testDeleteCategory() {
		Long id = 1L;
		TaskCategory category = new TaskCategory();
		category.setCategoryId(id);
		when(taskCategoryRepository.findById(id)).thenReturn(Optional.of(category));

		ResponseEntity<?> response = taskCategoryController.deleteCategory(id);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testCreateTask() {
		// Mocking
		Long categoryId = 1L;
		TaskUpdateDTO taskRequest = new TaskUpdateDTO();
		Task task = new Task();
		when(taskService.createTask(any(TaskUpdateDTO.class), anyLong())).thenReturn(task);

		// Test
		ResponseEntity<?> response = taskCategoryController.createTask(categoryId, taskRequest);

		// Assertion
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertFalse(response.getBody() == null);
	}


	@Test
	public void testCreateCategory_ValidationException() {
		TaskCategory taskCategory = new TaskCategory();

		Set<ConstraintViolation<TaskCategory>> violations = validator.validate(taskCategory);

		assertFalse(violations.isEmpty());
	}
}
