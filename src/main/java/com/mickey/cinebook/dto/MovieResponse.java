package com.mickey.cinebook.dto;

import com.mickey.cinebook.entity.Movie;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieResponse {
	private Long id;
	private String title;
	private String description;
	private String genre;
	private boolean isActive;
	private Instant createdAt;
	private Instant updatedAt;
	
	
}
