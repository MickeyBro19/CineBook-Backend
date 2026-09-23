package com.mickey.cinebook.controller;

import com.mickey.cinebook.dto.BookingRequest;
import com.mickey.cinebook.dto.BookingResponse;
import com.mickey.cinebook.service.BookingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
		name = "Bookings",
		description = "Movie ticket booking APIs"
)
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
	public ResponseEntity<Page<BookingResponse>> findMyBookings(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "asc") String direction) {
		Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		Pageable pageable = PageRequest.of(page, size, sort);
		
		return ResponseEntity.ok(
				bookingService.findMyBookings(pageable)
		                        );
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> cancel(
			@PathVariable Long id) {
		
		bookingService.cancel(id);
		
		return ResponseEntity.noContent().build();
	}
}