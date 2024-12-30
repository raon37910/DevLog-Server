package com.raon.devlog.controller.user.response;

import java.time.LocalDateTime;

public record CreateUserResponse(
	String email,
	String name,
	String description,
	String profileImageUrl,
	LocalDateTime createTime,
	LocalDateTime updateTime
) {

}
