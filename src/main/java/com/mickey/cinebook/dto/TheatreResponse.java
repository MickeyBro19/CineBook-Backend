package com.mickey.cinebook.dto;

import java.time.Instant;

public record TheatreResponse(
		Long id,
		String name,
		String address,
		String city,
		boolean isActive,
		Instant createdAt,
		Instant updatedAt
) {}