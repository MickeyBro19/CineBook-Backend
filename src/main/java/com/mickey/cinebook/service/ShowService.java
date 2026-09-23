package com.mickey.cinebook.service;

import com.mickey.cinebook.dto.ShowRequest;
import com.mickey.cinebook.dto.ShowResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShowService {
	
	Page<ShowResponse> findAll(Pageable pageable);
	
	ShowResponse findById(Long id);
	
	ShowResponse create(ShowRequest request);
	
	ShowResponse update(Long id, ShowRequest request);
	
	void delete(Long id);
}