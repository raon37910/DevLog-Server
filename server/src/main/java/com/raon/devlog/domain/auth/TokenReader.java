package com.raon.devlog.domain.auth;

import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.raon.devlog.config.JwtConfig;

@Component
public class TokenReader {
	private final JwtConfig jwtConfig;
	private final RedisTemplate<String, String> redisTemplate;

	public TokenReader(JwtConfig jwtConfig, RedisTemplate<String, String> redisTemplate) {
		this.jwtConfig = jwtConfig;
		this.redisTemplate = redisTemplate;
	}

	public Optional<String> getRefreshToken(String accessToken) {
		return Optional.ofNullable(redisTemplate.opsForValue().get(accessToken));
	}
}
