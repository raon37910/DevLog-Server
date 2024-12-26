package com.raon.devlog.controller.auth.response;

public record SinginResponse(
	String accessToken,
	String refreshToken
) {
}
