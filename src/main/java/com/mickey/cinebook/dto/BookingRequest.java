package com.mickey.cinebook.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record BookingRequest(
		
		@NotNull
		Long showId,
		
		@NotEmpty(message = "At least one seat must be selected")
		List<Long> seatIds

) {
}