package com.raon.devlog.service.auth.model;

public record Token(
	String accessToken,
	String refreshToken
) {
}
