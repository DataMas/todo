package com.cern.todo.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskCategoryDTO {

    Long id;

    @NotBlank(message = "Category name is required")
    @NotNull(message = "Category name is required")
    @Size(min=3, max=100, message = "Category name should be between 3 and 100 characters")
    String categoryName;

    @Size(max=200, message = "Category description should be 200 characters maximum")
    String categoryDescription;
}
