package com.mickey.cinebook.service;

import com.mickey.cinebook.dto.MovieRequest;
import com.mickey.cinebook.dto.MovieResponse;
import com.mickey.cinebook.entity.Movie;

import java.util.List;

public interface MovieService {
	List<MovieResponse> findAll();
	
	MovieResponse findById(Long id);
	
	MovieResponse create(MovieRequest movieRequest);
	
	MovieResponse update(Long id, MovieRequest movieRequest);
	
	void delete(Long id);
}
