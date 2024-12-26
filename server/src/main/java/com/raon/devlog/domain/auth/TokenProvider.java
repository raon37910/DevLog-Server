package com.raon.devlog.domain.auth;

import com.raon.devlog.service.auth.model.TokenClaim;

public interface TokenProvider {

	String generateAccessToken(TokenClaim tokenClaim);

	String generateRefreshToken(TokenClaim tokenClaim);

	TokenClaim parseToken(String token);
}
