package com.raon.devlog.mapper.article.category;

import java.time.LocalDateTime;

public record CategoryEntity(
	Long id,
	String name,
	LocalDateTime createTime,
	LocalDateTime updateTime
) {

	public static CategoryEntity from(String name) {
		return new CategoryEntity(null, name, LocalDateTime.now(), null);
	}
}
