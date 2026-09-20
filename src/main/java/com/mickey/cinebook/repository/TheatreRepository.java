package com.mickey.cinebook.repository;

import com.mickey.cinebook.entity.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TheatreRepository extends JpaRepository<Theatre, Long> {
	
	List<Theatre> findByIsActiveTrue();
	
	Optional<Theatre> findByIdAndIsActiveTrue(Long id);
	
	boolean existsByNameIgnoreCaseAndCityIgnoreCaseAndIdNot(
			String name,
			String city,
			Long id
	                                               );
	boolean existsByNameIgnoreCaseAndCityIgnoreCase(
			String name,
			String city
	                                               );
}
