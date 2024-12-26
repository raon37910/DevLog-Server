package com.raon.devlog.domain.auth;

import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.raon.devlog.config.JwtConfig;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider implements TokenProvider {

	private final JwtConfig jwtConfig;

	public JwtTokenProvider(JwtConfig jwtConfig) {
		this.jwtConfig = jwtConfig;
	}

	@Override
	public String generateAccessToken(String email, List<String> roles) {
		long now = System.currentTimeMillis();
		Date expireDate = new Date(now + jwtConfig.getAccessToken().expire());
		SecretKey secretKey = Keys.hmacShaKeyFor(jwtConfig.getAccessToken().secret().getBytes());

		return Jwts.builder()
			.subject(email)
			.claim("roles", roles)
			.issuedAt(new Date(now))
			.expiration(expireDate)
			.signWith(secretKey)
			.compact();
	}

	@Override
	public String generateRefreshToken(String email, List<String> roles) {
		long now = System.currentTimeMillis();
		Date expireDate = new Date(now + jwtConfig.getRefreshToken().expire());
		SecretKey secretKey = Keys.hmacShaKeyFor(jwtConfig.getRefreshToken().secret().getBytes());

		return Jwts.builder()
			.subject(email)
			.claim("roles", roles)
			.issuedAt(new Date(now))
			.expiration(expireDate)
			.signWith(secretKey)
			.compact();
	}
}
