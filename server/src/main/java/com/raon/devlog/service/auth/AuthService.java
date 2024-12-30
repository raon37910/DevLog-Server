package com.raon.devlog.service.auth;

import java.util.List;

import org.springframework.stereotype.Service;

import com.raon.devlog.component.auth.PasswordValidator;
import com.raon.devlog.component.auth.TokenProvider;
import com.raon.devlog.component.error.DevlogException;
import com.raon.devlog.component.error.ErrorType;
import com.raon.devlog.mapper.user.RoleEntity;
import com.raon.devlog.mapper.user.UserEntity;
import com.raon.devlog.repository.auth.TokenCommand;
import com.raon.devlog.repository.auth.TokenQuery;
import com.raon.devlog.repository.user.UserQuery;
import com.raon.devlog.service.auth.model.SigninRequestInfo;
import com.raon.devlog.service.auth.model.Token;
import com.raon.devlog.service.auth.model.TokenClaim;

@Service
public class AuthService {
	private final UserQuery userQuery;
	private final PasswordValidator passwordValidator;
	private final TokenProvider tokenProvider;
	private final TokenCommand tokenCommand;
	private final TokenQuery tokenQuery;

	public AuthService(
		UserQuery userQuery,
		PasswordValidator passwordValidator,
		TokenProvider tokenProvider,
		TokenCommand tokenCommand,
		TokenQuery tokenQuery
	) {
		this.userQuery = userQuery;
		this.passwordValidator = passwordValidator;
		this.tokenProvider = tokenProvider;
		this.tokenCommand = tokenCommand;
		this.tokenQuery = tokenQuery;
	}

	public Token generateToken(SigninRequestInfo signinRequestInfo) {
		UserEntity foundUser = userQuery.findByEmail(signinRequestInfo.email())
			.orElseThrow(() -> new DevlogException(ErrorType.VALIDATION_ERROR));

		List<String> userRoles = userQuery.getRoles(foundUser.id())
			.stream().map(RoleEntity::name)
			.toList();

		passwordValidator.validatePassword(signinRequestInfo.password(), foundUser.password());
		TokenClaim tokenClaim = new TokenClaim(foundUser.email(), userRoles);
		final Token token = tokenProvider.generateToken(tokenClaim);

		tokenCommand.appendToken(token);

		return token;
	}

	public Token refreshToken(String accessToken) {
		String refreshToken = tokenQuery.getRefreshToken(accessToken)
			.orElseThrow(() -> new DevlogException(ErrorType.AUTHENTICATION_ERROR));

		TokenClaim tokenClaim = tokenProvider.parseToken(refreshToken);
		final Token token = tokenProvider.generateToken(tokenClaim);

		tokenCommand.appendToken(token);

		return token;
	}
}
