package com.raon.devlog.component.auth;

import com.raon.devlog.service.auth.model.Token;
import com.raon.devlog.service.auth.model.TokenClaim;

public interface TokenProvider {

	String generateAccessToken(TokenClaim tokenClaim);

	String generateRefreshToken(TokenClaim tokenClaim);

	Token generateToken(TokenClaim tokenClaim);

	TokenClaim parseToken(String token);
}
