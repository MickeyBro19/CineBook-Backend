package com.mickey.cinebook.dto;

import com.mickey.cinebook.entity.enums.SeatType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SeatRequest(
		
		@NotBlank
		@Size(max = 10)
		String seatNumber,
		
		@NotBlank
		@Size(max = 5)
		String rowLabel,
		
		@NotNull
		SeatType seatType,
		
		@NotNull
		Long screenId
) {
}