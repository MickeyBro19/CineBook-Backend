package com.mickey.cinebook.service;

import com.mickey.cinebook.dto.MovieRequest;
import com.mickey.cinebook.dto.MovieResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieService {
	Page<MovieResponse> findAll(Pageable pageable);
	
	MovieResponse findById(Long id);
	
	MovieResponse create(MovieRequest movieRequest);
	
	MovieResponse update(Long id, MovieRequest movieRequest);
	
	void delete(Long id);
	
	MovieResponse findByName(String name);
}
