package com.mickey.cinebook.dto;

import java.time.Instant;

public record ScreenResponse(
		Long id,
		String name,
		Long theatreId,
		String theatreName,
		Boolean isActive,
		Instant createdAt,
		Instant updatedAt
) {
}
