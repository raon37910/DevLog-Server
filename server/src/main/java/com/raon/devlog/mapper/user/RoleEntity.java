package com.raon.devlog.mapper.user;

import java.time.LocalDateTime;

public record RoleEntity(
	Long id,
	String name,
	String description,
	LocalDateTime createTime,
	LocalDateTime updateTime
) {
}
