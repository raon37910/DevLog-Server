package com.raon.devlog.service.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raon.devlog.domain.user.UserAppender;
import com.raon.devlog.domain.user.UserReader;
import com.raon.devlog.mapper.user.UserEntity;
import com.raon.devlog.support.error.DevlogException;
import com.raon.devlog.support.error.ErrorType;

@Service
public class UserService {
	private final UserAppender userAppender;
	private final UserReader userReader;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserAppender userAppender, UserReader userReader, PasswordEncoder passwordEncoder) {
		this.userAppender = userAppender;
		this.userReader = userReader;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public UserInfo createUser(CreateUserInfo createUserInfo) {
		if (userReader.existsByEmail(createUserInfo.email())) {
			throw new DevlogException(ErrorType.VALIDATION_ERROR);
		}

		CreateUserInfo hashedUserInfo = new CreateUserInfo(
			createUserInfo.email(),
			passwordEncoder.encode(createUserInfo.password())
		);

		userAppender.append(hashedUserInfo.toEntity());

		UserEntity userEntity = userReader.findByEmail(createUserInfo.email())
			.orElseThrow(() -> new DevlogException(ErrorType.DEFAULT_ERROR));

		return UserInfo.from(userEntity);
	}
}
