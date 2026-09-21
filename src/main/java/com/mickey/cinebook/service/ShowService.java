package com.mickey.cinebook.service;

import com.mickey.cinebook.dto.ShowRequest;
import com.mickey.cinebook.dto.ShowResponse;

import java.util.List;

public interface ShowService {
	
	List<ShowResponse> findAll();
	
	ShowResponse findById(Long id);
	
	ShowResponse create(ShowRequest request);
	
	ShowResponse update(Long id, ShowRequest request);
	
	void delete(Long id);
}