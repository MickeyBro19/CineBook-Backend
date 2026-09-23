package com.mickey.cinebook.service;

import com.mickey.cinebook.dto.BookingRequest;
import com.mickey.cinebook.dto.BookingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookingService {
	
	BookingResponse create(BookingRequest request);
	
	BookingResponse findById(Long id);
	
	Page<BookingResponse> findMyBookings(Pageable pageable);
	
	void cancel(Long id);
}