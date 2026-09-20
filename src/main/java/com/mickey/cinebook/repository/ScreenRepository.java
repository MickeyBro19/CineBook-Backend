package com.mickey.cinebook.repository;

import com.mickey.cinebook.entity.Screen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScreenRepository extends JpaRepository<Screen, Long> {
	List<Screen> findByIsActiveTrue();
	
	Optional<Screen> findByIdAndIsActive(Long id,boolean isActive);
	
	boolean existsByTheatreIdAndNameIgnoreCase(
			Long theatreId,
			String name
	                                          );
	
	boolean existsByTheatreIdAndNameIgnoreCaseAndIdNot(
			Long theatreId,
			String name,
			Long id
	                                                  );
}
