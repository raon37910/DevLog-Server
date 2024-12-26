package com.raon.devlog.controller.user.response;

import java.time.LocalDateTime;

import com.raon.devlog.service.user.UserInfo;

public record CreateUserResponse(
	String email,
	String name,
	String description,
	String profileImageUrl,
	LocalDateTime createTime,
	LocalDateTime updateTime
) {
	public static CreateUserResponse from(UserInfo userInfo) {
		return new CreateUserResponse(
			userInfo.email(),
			userInfo.name(),
			userInfo.description(),
			userInfo.profileImageUrl(),
			userInfo.createTime(),
			userInfo.updateTime()
		);
	}
}
