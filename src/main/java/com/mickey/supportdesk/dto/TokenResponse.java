package com.mickey.supportdesk.dto;

public record TokenResponse(
		String accessToken,
		String refreshToken,
		Long expiresIn,
		String tokenTyp,
		UserResponseDto user
) {
	public static TokenResponse of(String accessToken, String refreshToken, Long expiresIn, String tokenTyp, UserResponseDto user) {
		return new TokenResponse(accessToken, refreshToken, expiresIn, tokenTyp, user);
	}
}
