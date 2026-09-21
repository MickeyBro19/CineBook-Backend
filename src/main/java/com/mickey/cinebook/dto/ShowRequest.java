package com.mickey.cinebook.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ShowRequest(
		
		@NotNull
		Long movieId,
		
		@NotNull
		Long screenId,
		
		@NotNull
		@Future(message = "Start time must be in the future")
		LocalDateTime startTime,
		
		@NotNull
		@Future(message = "End time must be in the future")
		LocalDateTime endTime,
		
		@NotNull
		@DecimalMin(value = "0.0", inclusive = false,
				message = "Ticket price must be greater than 0")
		BigDecimal ticketPrice
) {
}