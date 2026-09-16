package com.mickey.cinebook.dto;

public record TokenResponseDto(
		String accessToken,
		String refreshToken,
		Long expiresIn,
		String tokenTyp,
		UserResponseDto user
) {
	public static TokenResponseDto of(String accessToken, String refreshToken, Long expiresIn, String tokenTyp, UserResponseDto user) {
		return new TokenResponseDto(accessToken, refreshToken, expiresIn, tokenTyp, user);
	}
}
