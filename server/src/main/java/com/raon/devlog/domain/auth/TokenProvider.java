package com.raon.devlog.domain.auth;

import java.util.List;

public interface TokenProvider {

	String generateAccessToken(String email, List<String> roles);

	String generateRefreshToken(String email, List<String> roles);
}
