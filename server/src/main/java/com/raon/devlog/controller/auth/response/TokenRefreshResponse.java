package com.raon.devlog.controller.auth.response;

public record TokenRefreshResponse(
	String accessToken,
	String refreshToken
) {
}
