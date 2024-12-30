package com.raon.devlog.repository.auth;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.raon.devlog.config.JwtConfig;
import com.raon.devlog.service.auth.model.Token;

@Repository
public class TokenCommand {
	private final JwtConfig jwtConfig;
	private final RedisTemplate<String, String> redisTemplate;

	public TokenCommand(JwtConfig jwtConfig, RedisTemplate<String, String> redisTemplate) {
		this.jwtConfig = jwtConfig;
		this.redisTemplate = redisTemplate;
	}

	public void appendToken(Token token) {
		redisTemplate.opsForValue()
			.set(
				token.accessToken(),
				token.refreshToken(),
				jwtConfig.getRefreshToken().expire(),
				TimeUnit.MICROSECONDS
			);
	}
}
