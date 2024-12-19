package com.raon.devlog.service.user;

import java.time.LocalDateTime;

import com.raon.devlog.mapper.user.UserEntity;

public record UserInfo(
	Long id,
	String email,
	String name,
	String description,
	String profileImageUrl,
	LocalDateTime createTime,
	LocalDateTime updateTime
) {
	public static UserInfo from(UserEntity entity) {
		return new UserInfo(
			entity.id(),
			entity.email(),
			entity.name(),
			entity.description(),
			entity.profileImageUrl(),
			entity.createTime(),
			entity.updateTime()
		);
	}
}
