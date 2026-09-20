package com.mickey.cinebook.service;

import com.mickey.cinebook.dto.ScreenRequest;
import com.mickey.cinebook.dto.ScreenResponse;

import java.util.List;

public interface ScreenService {
	List<ScreenResponse> findAll();
	
	ScreenResponse findById(Long id);
	
	ScreenResponse create(ScreenRequest request);
	
	ScreenResponse update(Long id, ScreenRequest request);
	
	void delete(Long id);
}
