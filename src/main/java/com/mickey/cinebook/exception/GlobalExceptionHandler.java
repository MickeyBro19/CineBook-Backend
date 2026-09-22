package com.mickey.cinebook.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	//404 -Not Found
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e, HttpServletRequest request) {
		return buildResponse(
				HttpStatus.NOT_FOUND,
				e.getMessage(),
				request.getRequestURI(),
				null
		                    );
	}
	
	//404 -Not Found
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUserNotFoundException (UserNotFoundException e, HttpServletRequest request) {
		return buildResponse(
				HttpStatus.NOT_FOUND,
				"User not found",
				request.getRequestURI(),
				null
		                    );
	}
	
	//400- Email Already Exists
	@ExceptionHandler(EmailAlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException (EmailAlreadyExistsException e, HttpServletRequest request) {
		return buildResponse(
				HttpStatus.BAD_REQUEST,
				"User not found",
				request.getRequestURI(),
				null
		                    );
	}
	
	// 400- Business/ validation error/ bad request
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
		return buildResponse(
				HttpStatus.valueOf(400),
				e.getMessage(),
				request.getRequestURI(),
				null
		                    );
	}
	
	// 400 @Valid validation error
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
		Map<String, String> errors = new HashMap<>();
		e.getBindingResult().getFieldErrors().forEach((fieldError) -> {
			errors.put(fieldError.getField(), fieldError.getDefaultMessage());
		});
		return buildResponse(
				HttpStatus.BAD_REQUEST,
				"Validation Failed",
				request.getRequestURI(),
				errors
		                    );
	}
	
	// 400 - Invalid JSON/body
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleInvalidRequestBody(
			HttpMessageNotReadableException ex,
			HttpServletRequest request) {
		
		return buildResponse(
				HttpStatus.BAD_REQUEST,
				"Invalid request body",
				request.getRequestURI(),
				null
		                    );
	}
	
	// 400 - Invalid path variable type
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleTypeMismatch(
			MethodArgumentTypeMismatchException ex,
			HttpServletRequest request) {
		
		String message = "Invalid value for parameter: " + ex.getName();
		
		return buildResponse(
				HttpStatus.BAD_REQUEST,
				message,
				request.getRequestURI(),
				null
		                    );
	}
	
	// 500 - Unexpected errors
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(
			Exception ex,
			HttpServletRequest request) {
		
		return buildResponse(
				HttpStatus.INTERNAL_SERVER_ERROR,
				"An unexpected error occurred",
				request.getRequestURI(),
				null
		                    );
	}
	
	
	
	
	private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, String path, Map<String, String> validationErrors) {
		ErrorResponse response = ErrorResponse.builder()
				.timeStamp(Instant.now())
				.status(status.value())
				.error(status.getReasonPhrase())
				.message(message)
				.path(path)
				.validationErrors(validationErrors)
				.build();
		return ResponseEntity.status(status).body(response);
	}
}
