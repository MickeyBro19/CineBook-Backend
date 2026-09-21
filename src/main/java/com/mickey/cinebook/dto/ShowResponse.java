package com.mickey.cinebook.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

public record ShowResponse(
		
		Long id,
		
		Long movieId,
		String movieTitle,
		
		Long screenId,
		String screenName,
		
		Long theatreId,
		String theatreName,
		
		LocalDateTime startTime,
		LocalDateTime endTime,
		
		BigDecimal ticketPrice,
		
		boolean isActive,
		
		Instant createdAt,
		Instant updatedAt
) {
}