package com.mickey.cinebook.exception;

import lombok.Builder;

import java.time.Instant;
import java.util.Map;

@Builder
public record ErrorResponse(
		Instant timeStamp,
		int status,
		String error,
		String message,
		String path,
		Map<String, String> validationErrors
) {
}
