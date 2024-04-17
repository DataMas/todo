package com.cern.todo;

import com.cern.todo.Controller.TaskCategoryController;
import com.cern.todo.Entities.TaskCategory;
import com.cern.todo.Repository.TaskCategoryRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Set;

@ExtendWith(MockitoExtension.class)
class TodoApplicationTests {

	@Mock
	private TaskCategoryRepository taskCategoryRepository;
	@InjectMocks
	private TaskCategoryController taskCategoryController;

	private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


	@Test
	void contextLoads() {
	}

	@Test
	public void testCreateCategory() {

		TaskCategory category = new TaskCategory();
		category.setCategoryId(1L);
		category.setCategoryName("Test Category");

		when(taskCategoryRepository.save(any(TaskCategory.class))).thenReturn(category);

		ResponseEntity<?> response = taskCategoryController.createCategory(category);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(category, response.getBody());
	}

	@Test
	public void testCreateCategory_ValidationException() {
		TaskCategory taskCategory = new TaskCategory();

		Set<ConstraintViolation<TaskCategory>> violations = validator.validate(taskCategory);

		assertFalse(violations.isEmpty());
	}
}
