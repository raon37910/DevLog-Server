package com.raon.devlog.domain.auth;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.raon.devlog.config.JwtConfig;
import com.raon.devlog.service.auth.model.Token;

@Component
public class TokenAppender {

	private final JwtConfig jwtConfig;
	private final RedisTemplate<String, String> redisTemplate;

	public TokenAppender(JwtConfig jwtConfig, RedisTemplate<String, String> redisTemplate) {
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
