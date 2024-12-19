package com.raon.devlog.service.user;

import java.time.LocalDateTime;

import com.raon.devlog.mapper.user.UserEntity;

public record CreateUserInfo(
	String email,
	String password
) {
	public UserEntity toEntity() {
		return UserEntity.from(email, password, LocalDateTime.now());
	}
}
