package com.mickey.cinebook.repository;

import com.mickey.cinebook.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ShowRepository extends JpaRepository<Show, Long> {
	
	List<Show> findByIsActiveTrue();
	
	Optional<Show> findByIdAndIsActiveTrue(Long id);
	
	@Query("""
            SELECT s
            FROM Show s
            WHERE s.screen.id = :screenId
              AND s.isActive = true
              AND s.startTime < :endTime
              AND s.endTime > :startTime
            """)
	List<Show> findOverlappingShows(
			@Param("screenId") Long screenId,
			@Param("startTime") java.time.LocalDateTime startTime,
			@Param("endTime") java.time.LocalDateTime endTime
	                               );
	
	@Query("""
            SELECT s
            FROM Show s
            WHERE s.screen.id = :screenId
              AND s.isActive = true
              AND s.id <> :showId
              AND s.startTime < :endTime
              AND s.endTime > :startTime
            """)
	List<Show> findOverlappingShowsForUpdate(
			@Param("screenId") Long screenId,
			@Param("showId") Long showId,
			@Param("startTime") java.time.LocalDateTime startTime,
			@Param("endTime") java.time.LocalDateTime endTime
	                                        );
}