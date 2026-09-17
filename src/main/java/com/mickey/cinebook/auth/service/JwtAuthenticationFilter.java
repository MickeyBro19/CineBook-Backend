package com.mickey.cinebook.auth.service;


import com.mickey.cinebook.auth.entity.User;
import com.mickey.cinebook.auth.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtService jwtService;
	private final UserRepository userRepository;
	
	public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
		this.jwtService = jwtService;
		this.userRepository = userRepository;
	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return request.getRequestURI().startsWith("/api/auth");
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String header = request.getHeader("Authorization");
		if (header != null && header.startsWith("Bearer ")) {
			String token = header.substring(7);
			try {
				if (!jwtService.isAccessToken(token)) {
					filterChain.doFilter(request, response);
					return;
				}
				Long userId = jwtService.getUserId(token);
				User user = userRepository.findById(userId)
						.orElseThrow(() -> new BadCredentialsException("User not found"));
				if (!user.isEnabled()) {
					throw new DisabledException("User is disabled");
				}
				List<GrantedAuthority> authorities = user.getRole() == null ? List.of() : List.of(new SimpleGrantedAuthority(user.getRole().name()));
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
			catch (Exception e) {
				e.printStackTrace();
				throw new BadCredentialsException("Invalid token");
			}
		}
		filterChain.doFilter(request, response);
	}
}
