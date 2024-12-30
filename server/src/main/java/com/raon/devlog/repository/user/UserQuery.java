package com.raon.devlog.repository.user;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.raon.devlog.component.error.DevlogException;
import com.raon.devlog.component.error.ErrorType;
import com.raon.devlog.mapper.user.RoleEntity;
import com.raon.devlog.mapper.user.UserEntity;
import com.raon.devlog.mapper.user.UserMapper;

@Repository
public class UserQuery {
	private final UserMapper userMapper;

	public UserQuery(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	public boolean existsByEmail(String email) {
		return userMapper.existsByEmail(email);
	}

	public void ifExistsByEmailDoThrow(String email) {
		if (existsByEmail(email)) {
			throw new DevlogException(ErrorType.VALIDATION_ERROR);
		}
	}

	public Optional<UserEntity> findByEmail(String email) {
		return Optional.ofNullable(userMapper.findByEmail(email));
	}

	public List<RoleEntity> getRoles(Long userId) {
		return userMapper.getRoles(userId);
	}
}
