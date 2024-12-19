package com.raon.devlog.domain.user;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.raon.devlog.mapper.user.UserEntity;
import com.raon.devlog.mapper.user.UserMapper;

@Component
public class UserReader {
	private final UserMapper userMapper;

	public UserReader(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	public Optional<UserEntity> findByEmail(String email) {
		return Optional.ofNullable(userMapper.findByEmail(email));
	}

	public boolean existsByEmail(String email) {
		return userMapper.existsByEmail(email);
	}
}
