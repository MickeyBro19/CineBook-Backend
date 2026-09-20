package com.mickey.cinebook.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TheatreRequest(
		@NotBlank
		@Size(max = 100)
		String name,
		
		@NotBlank
		@Size(max = 255)
		String address,
		
		@NotBlank
		@Size(max = 100)
		String city
) {}