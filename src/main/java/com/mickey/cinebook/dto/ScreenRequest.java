package com.mickey.cinebook.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ScreenRequest(
		@NotBlank
		@Size(min = 3, max = 100, message = "Size should be between 3 and 100 characters")
		String name,
		
		@NotNull
		Long theatreId
) {
}
