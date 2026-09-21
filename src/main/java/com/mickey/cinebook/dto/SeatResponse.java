package com.mickey.cinebook.dto;

import com.mickey.cinebook.entity.enums.SeatType;

import java.time.Instant;

public record SeatResponse(
		Long id,
		String seatNumber,
		String rowLabel,
		SeatType seatType,
		Long screenId,
		String screenName,
		Boolean isActive,
		Instant createdAt,
		Instant updatedAt
) {
}