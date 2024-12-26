package com.raon.devlog.service.auth;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.raon.devlog.domain.auth.TokenAppender;
import com.raon.devlog.domain.auth.TokenProvider;
import com.raon.devlog.domain.user.UserReader;
import com.raon.devlog.mapper.user.RoleEntity;
import com.raon.devlog.mapper.user.UserEntity;
import com.raon.devlog.service.auth.model.SigninRequestInfo;
import com.raon.devlog.service.auth.model.Token;
import com.raon.devlog.support.error.DevlogException;
import com.raon.devlog.support.error.ErrorType;

@Service
public class AuthService {
	// TODO 클래스 포함 관계가 너무 많아지는데
	private final UserReader userReader;
	private final PasswordEncoder passwordEncoder;
	private final TokenProvider tokenProvider;
	private final TokenAppender tokenAppender;

	public AuthService(
		UserReader userReader,
		PasswordEncoder passwordEncoder,
		TokenProvider tokenProvider,
		TokenAppender tokenAppender
	) {
		this.userReader = userReader;
		this.passwordEncoder = passwordEncoder;
		this.tokenProvider = tokenProvider;
		this.tokenAppender = tokenAppender;
	}

	public Token generateToken(SigninRequestInfo signinRequestInfo) {
		UserEntity foundUser = userReader.findByEmail(signinRequestInfo.email())
			.orElseThrow(() -> new DevlogException(ErrorType.VALIDATION_ERROR));
		List<String> userRoles = userReader.getRoles(foundUser.id())
			.stream().map(RoleEntity::name)
			.toList();

		if (!passwordEncoder.matches(signinRequestInfo.password(), foundUser.password())) {
			throw new DevlogException(ErrorType.AUTHENTICATION_ERROR);
		}

		final String accessToken = tokenProvider.generateAccessToken(foundUser.email(), userRoles);
		final String refreshToken = tokenProvider.generateRefreshToken(foundUser.email(), userRoles);
		final Token token = new Token(accessToken, refreshToken);
		tokenAppender.appendToken(token);

		return token;
	}
}
