package com.mickey.cinebook.dto;

import com.mickey.cinebook.entity.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record BookingResponse(
		
		Long id,
		
		Long userId,
		
		Long showId,
		
		String movieTitle,
		
		String theatreName,
		
		String screenName,
		
		List<Long> seatIds,
		
		BigDecimal totalAmount,
		
		BookingStatus status,
		
		Instant createdAt

) {
}