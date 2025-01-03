package com.raon.devlog.mapper.article.bookmark;

import java.time.LocalDateTime;

public record ArticleBookMarkEntity(
	Long id,
	Long userId,
	Long articleId,
	LocalDateTime createTime,
	LocalDateTime updateTime
) {
	public static ArticleBookMarkEntity from(Long userId, Long articleId) {
		return new ArticleBookMarkEntity(null, userId, articleId, LocalDateTime.now(), null);
	}
}
