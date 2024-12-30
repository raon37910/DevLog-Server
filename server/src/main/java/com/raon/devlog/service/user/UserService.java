package com.raon.devlog.service.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raon.devlog.mapper.user.UserEntity;
import com.raon.devlog.repository.user.UserCommand;
import com.raon.devlog.repository.user.UserQuery;

@Service
public class UserService {
	private final UserCommand userCommand;
	private final UserQuery userQuery;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserCommand userCommand, UserQuery userQuery, PasswordEncoder passwordEncoder) {
		this.userCommand = userCommand;
		this.userQuery = userQuery;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public void createUser(CreateUserInfo createUserInfo) {
		userQuery.ifExistsByEmailDoThrow(createUserInfo.email());
		userCommand.append(
			UserEntity.from(createUserInfo.email(), passwordEncoder.encode(createUserInfo.password())
			));
	}
}
