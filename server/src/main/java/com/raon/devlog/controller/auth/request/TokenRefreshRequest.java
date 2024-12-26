package com.raon.devlog.controller.auth.request;

import jakarta.validation.constraints.NotBlank;

public record TokenRefreshRequest(
	@NotBlank
	String accessToken
) {
}
