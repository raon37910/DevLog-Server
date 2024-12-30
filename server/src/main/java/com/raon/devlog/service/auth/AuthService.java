package com.raon.devlog.service.auth;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.raon.devlog.domain.auth.TokenAppender;
import com.raon.devlog.domain.auth.TokenProvider;
import com.raon.devlog.domain.auth.TokenReader;
import com.raon.devlog.mapper.user.RoleEntity;
import com.raon.devlog.mapper.user.UserEntity;
import com.raon.devlog.repository.user.UserQuery;
import com.raon.devlog.service.auth.model.SigninRequestInfo;
import com.raon.devlog.service.auth.model.Token;
import com.raon.devlog.service.auth.model.TokenClaim;
import com.raon.devlog.support.error.DevlogException;
import com.raon.devlog.support.error.ErrorType;

@Service
public class AuthService {
	private final UserQuery userQuery;
	private final PasswordEncoder passwordEncoder;
	private final TokenProvider tokenProvider;
	private final TokenAppender tokenAppender;
	private final TokenReader tokenReader;

	public AuthService(
		UserQuery userQuery,
		PasswordEncoder passwordEncoder,
		TokenProvider tokenProvider,
		TokenAppender tokenAppender,
		TokenReader tokenReader
	) {
		this.userQuery = userQuery;
		this.passwordEncoder = passwordEncoder;
		this.tokenProvider = tokenProvider;
		this.tokenAppender = tokenAppender;
		this.tokenReader = tokenReader;
	}

	public Token generateToken(SigninRequestInfo signinRequestInfo) {
		UserEntity foundUser = userQuery.findByEmail(signinRequestInfo.email())
			.orElseThrow(() -> new DevlogException(ErrorType.VALIDATION_ERROR));
		List<String> userRoles = userQuery.getRoles(foundUser.id())
			.stream().map(RoleEntity::name)
			.toList();

		if (!passwordEncoder.matches(signinRequestInfo.password(), foundUser.password())) {
			throw new DevlogException(ErrorType.AUTHENTICATION_ERROR);
		}

		TokenClaim tokenClaim = new TokenClaim(foundUser.email(), userRoles);

		final String accessToken = tokenProvider.generateAccessToken(tokenClaim);
		final String refreshToken = tokenProvider.generateRefreshToken(tokenClaim);
		final Token token = new Token(accessToken, refreshToken);
		tokenAppender.appendToken(token);

		return token;
	}

	public Token refreshToken(String accessToken) {
		String refreshToken = tokenReader.getRefreshToken(accessToken)
			.orElseThrow(() -> new DevlogException(ErrorType.AUTHENTICATION_ERROR));

		TokenClaim tokenClaim = tokenProvider.parseToken(refreshToken);

		final String newAccessToken = tokenProvider.generateAccessToken(tokenClaim);
		final String newRefreshToken = tokenProvider.generateRefreshToken(tokenClaim);
		final Token token = new Token(newAccessToken, newRefreshToken);
		tokenAppender.appendToken(token);

		return token;
	}
}
