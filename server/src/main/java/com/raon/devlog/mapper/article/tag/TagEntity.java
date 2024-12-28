package com.raon.devlog.mapper.article.tag;

import java.time.LocalDateTime;

public record TagEntity(
	Long id,
	String name,
	LocalDateTime createTime,
	LocalDateTime updateTime
) {
	public static TagEntity from(String name) {
		return new TagEntity(null, name, LocalDateTime.now(), null);
	}
}
