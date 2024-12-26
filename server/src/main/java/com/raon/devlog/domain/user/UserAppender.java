package com.raon.devlog.domain.user;

import org.springframework.stereotype.Component;

import com.raon.devlog.mapper.user.UserEntity;
import com.raon.devlog.mapper.user.UserMapper;
import com.raon.devlog.mapper.user.UserRoleMapper;

@Component
public class UserAppender {
	private final UserMapper userMapper;
	private final UserRoleMapper userRoleMapper;
	private static final String ROLE_USER = "ROLE_USER";

	public UserAppender(UserMapper userMapper, UserRoleMapper userRoleMapper) {
		this.userMapper = userMapper;
		this.userRoleMapper = userRoleMapper;
	}

	// TODO 코드 개선 필요
	public void append(UserEntity user) {
		Long roleId = userRoleMapper.getRoleId(ROLE_USER);
		userMapper.create(user);
		Long userId = userMapper.findByEmail(user.email()).id();
		userRoleMapper.defaultUser(userId, roleId);
	}
}
