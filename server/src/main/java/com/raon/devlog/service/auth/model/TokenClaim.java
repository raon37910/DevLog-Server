package com.raon.devlog.service.auth.model;

import java.util.List;

public record TokenClaim(
	String subject,
	List<String> roles
) {
}
