package com.mickey.cinebook.service;

import com.mickey.cinebook.dto.ScreenRequest;
import com.mickey.cinebook.dto.ScreenResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ScreenService {
	Page<ScreenResponse> findAll(Pageable pageable);
	
	ScreenResponse findById(Long id);
	
	ScreenResponse create(ScreenRequest request);
	
	ScreenResponse update(Long id, ScreenRequest request);
	
	void delete(Long id);
}
