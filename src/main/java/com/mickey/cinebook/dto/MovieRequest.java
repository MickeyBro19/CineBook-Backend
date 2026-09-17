package com.mickey.cinebook.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MovieRequest(
		@NotBlank(message = "Please enter movie title")
		@Size(max = 100, message = "Title cannot exceed 100 characters")
		String title,
		
		@Size(max = 1000, message = "Description cannot exceed 1000 characters")
		String description,
		
		@NotBlank(message = "Please enter genre")
		String genre
) {
}
