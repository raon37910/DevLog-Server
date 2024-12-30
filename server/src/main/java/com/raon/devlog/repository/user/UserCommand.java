package com.raon.devlog.repository.user;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.raon.devlog.mapper.user.UserEntity;
import com.raon.devlog.mapper.user.UserMapper;
import com.raon.devlog.mapper.user.UserRoleMapper;

@Repository
public class UserCommand {
	private final UserMapper userMapper;
	private final UserRoleMapper userRoleMapper;
	private static final String ROLE_USER = "ROLE_USER";

	public UserCommand(UserMapper userMapper, UserRoleMapper userRoleMapper) {
		this.userMapper = userMapper;
		this.userRoleMapper = userRoleMapper;
	}

	@Transactional
	public void append(UserEntity user) {
		Long roleId = userRoleMapper.getRoleId(ROLE_USER);
		userMapper.create(user);
		Long userId = userMapper.findByEmail(user.email()).id();
		userRoleMapper.defaultUser(userId, roleId);
	}
}
