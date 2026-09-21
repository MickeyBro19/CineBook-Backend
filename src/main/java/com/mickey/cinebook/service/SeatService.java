package com.mickey.cinebook.service;

import com.mickey.cinebook.dto.SeatRequest;
import com.mickey.cinebook.dto.SeatResponse;

import java.util.List;

public interface SeatService {
	
	List<SeatResponse> findAll();
	
	List<SeatResponse> findByScreen(Long screenId);
	
	SeatResponse findById(Long id);
	
	SeatResponse create(SeatRequest request);
	
	SeatResponse update(Long id, SeatRequest request);
	
	void delete(Long id);
}