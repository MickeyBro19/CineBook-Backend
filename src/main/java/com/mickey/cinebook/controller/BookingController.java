package com.mickey.cinebook.controller;

import com.mickey.cinebook.dto.BookingRequest;
import com.mickey.cinebook.dto.BookingResponse;
import com.mickey.cinebook.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {
	
	private final BookingService bookingService;
	
	@PostMapping
	public ResponseEntity<BookingResponse> create(
			@Valid @RequestBody BookingRequest request) {
		
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(bookingService.create(request));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<BookingResponse> findById(
			@PathVariable Long id) {
		
		return ResponseEntity.ok(
				bookingService.findById(id)
		                        );
	}
	
	@GetMapping("/my")
	public ResponseEntity<List<BookingResponse>> findMyBookings() {
		
		return ResponseEntity.ok(
				bookingService.findMyBookings()
		                        );
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> cancel(
			@PathVariable Long id) {
		
		bookingService.cancel(id);
		
		return ResponseEntity.noContent().build();
	}
}