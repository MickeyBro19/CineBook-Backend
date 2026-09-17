package com.mickey.cinebook.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Setter
@Table(name = "movies")
public class Movie {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false,length = 100)
	private String title;
	
	private String description;
	
	@Column(nullable = false)
	private String genre;
	
	@Column(nullable = false)
	@Builder.Default
	private boolean isActive = true;
	
	@Column(nullable = false,updatable = false)
	private Instant createdAt;
	@Column(nullable = false)
	private Instant updatedAt;
	
	@PrePersist
	public void prePersist() {
		createdAt = Instant.now();
		updatedAt = Instant.now();
	}
	@PreUpdate
	public void preUpdate() {
		updatedAt = Instant.now();
	}

}
