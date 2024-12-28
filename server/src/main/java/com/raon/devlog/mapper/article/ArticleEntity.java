package com.raon.devlog.mapper.article;

import java.time.LocalDateTime;

public record ArticleEntity(
	Long id,
	String title,
	String author,
	String description,
	String link,
	Long views,
	Long categoryId,
	LocalDateTime createTime,
	LocalDateTime updateTime
) {
}
