package com.raon.devlog.domain.user;

import org.springframework.stereotype.Component;

import com.raon.devlog.mapper.user.UserEntity;
import com.raon.devlog.mapper.user.UserMapper;

@Component
public class UserAppender {
	private final UserMapper userMapper;

	public UserAppender(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	public void append(UserEntity user) {
		userMapper.create(user);
	}
}
