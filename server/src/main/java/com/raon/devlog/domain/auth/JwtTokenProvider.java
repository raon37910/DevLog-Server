package com.raon.devlog.domain.auth;

import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.raon.devlog.config.JwtConfig;
import com.raon.devlog.service.auth.model.TokenClaim;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider implements TokenProvider {

	private final JwtConfig jwtConfig;

	public JwtTokenProvider(JwtConfig jwtConfig) {
		this.jwtConfig = jwtConfig;
	}

	@Override
	public String generateAccessToken(TokenClaim tokenClaim) {
		long now = System.currentTimeMillis();
		Date expireDate = new Date(now + jwtConfig.getAccessToken().expire());
		SecretKey secretKey = Keys.hmacShaKeyFor(jwtConfig.getAccessToken().secret().getBytes());

		return Jwts.builder()
			.subject(tokenClaim.subject())
			.claim("roles", tokenClaim.roles())
			.issuedAt(new Date(now))
			.expiration(expireDate)
			.signWith(secretKey)
			.compact();
	}

	@Override
	public String generateRefreshToken(TokenClaim tokenClaim) {
		long now = System.currentTimeMillis();
		Date expireDate = new Date(now + jwtConfig.getRefreshToken().expire());
		SecretKey secretKey = Keys.hmacShaKeyFor(jwtConfig.getRefreshToken().secret().getBytes());

		return Jwts.builder()
			.subject(tokenClaim.subject())
			.claim("roles", tokenClaim.roles())
			.issuedAt(new Date(now))
			.expiration(expireDate)
			.signWith(secretKey)
			.compact();
	}

	@Override
	public TokenClaim parseToken(String token) {
		SecretKey secretKey = Keys.hmacShaKeyFor(jwtConfig.getRefreshToken().secret().getBytes());
		Jws<Claims> claimsJws = Jwts.parser().verifyWith(secretKey).build()
			.parseSignedClaims(token);

		String email = claimsJws.getPayload().getSubject();
		List<?> roles = claimsJws.getPayload().get("roles", List.class);

		return new TokenClaim(email, roles.stream().map(Object::toString).toList());
	}
}
