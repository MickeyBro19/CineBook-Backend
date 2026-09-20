package com.mickey.cinebook.service;

import com.mickey.cinebook.dto.TheatreRequest;
import com.mickey.cinebook.dto.TheatreResponse;

import java.util.List;

public interface TheatreService {
	
	List<TheatreResponse> findAll();
	
	TheatreResponse findById(Long id);
	
	TheatreResponse create(TheatreRequest request);
	
	TheatreResponse update(Long id, TheatreRequest request);
	
	void delete(Long id);
}
