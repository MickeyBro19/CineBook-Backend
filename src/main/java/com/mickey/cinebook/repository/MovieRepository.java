package com.mickey.cinebook.repository;


import com.mickey.cinebook.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
	Optional<Movie> findByTitleIgnoreCase(String title);
	
	boolean existsByTitleIgnoreCase(String title);
	
	List<Movie> findByIsActiveTrue();
	
}
