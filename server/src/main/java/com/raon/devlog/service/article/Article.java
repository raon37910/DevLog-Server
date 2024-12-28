package com.raon.devlog.service.article;

import java.time.LocalDateTime;

public record Article(
	Long id,
	String title,
	String description,
	String link,
	String author,
	Long views,
	LocalDateTime createTime,
	LocalDateTime updateTime
) {
}
