package com.raon.devlog.repository.auth;

import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TokenQuery {
	private final RedisTemplate<String, String> redisTemplate;

	public TokenQuery(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public Optional<String> getRefreshToken(String accessToken) {
		return Optional.ofNullable(redisTemplate.opsForValue().get(accessToken));
	}
}
