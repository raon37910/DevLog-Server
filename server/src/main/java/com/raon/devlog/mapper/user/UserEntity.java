package com.raon.devlog.mapper.user;

import java.time.LocalDateTime;

public record UserEntity(
	Long id,
	String email,
	String password,
	String name,
	String description,
	String profileImageUrl,
	LocalDateTime createTime,
	LocalDateTime updateTime
) {

	public static UserEntity from(String email, String password) {
		return new UserEntity(null, email, password, null, null, null, null, null);
	}

	public static UserEntity from(String email, String password, LocalDateTime createTime) {
		return new UserEntity(null, email, password, null, null, null, createTime, null);
	}
}
