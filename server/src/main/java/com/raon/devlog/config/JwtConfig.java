package com.raon.devlog.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

	private final AccessToken accessToken;
	private final RefreshToken refreshToken;

	public JwtConfig(AccessToken accessToken, RefreshToken refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public record AccessToken(String secret, long expire) {
	}

	public record RefreshToken(String secret, long expire) {
	}

	public AccessToken getAccessToken() {
		return accessToken;
	}

	public RefreshToken getRefreshToken() {
		return refreshToken;
	}

}
