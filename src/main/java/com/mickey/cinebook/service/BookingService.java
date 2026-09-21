package com.mickey.cinebook.service;

import com.mickey.cinebook.dto.BookingRequest;
import com.mickey.cinebook.dto.BookingResponse;

import java.util.List;

public interface BookingService {
	
	BookingResponse create(BookingRequest request);
	
	BookingResponse findById(Long id);
	
	List<BookingResponse> findMyBookings();
	
	void cancel(Long id);
}