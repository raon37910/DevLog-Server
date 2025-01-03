package com.raon.devlog.mapper.article.like;

import java.time.LocalDateTime;

public record ArticleLikeEntity(
	Long id,
	Long userId,
	Long articleId,
	LocalDateTime createTime,
	LocalDateTime updateTime
) {

	public static ArticleLikeEntity from(Long userId, Long articleId) {
		return new ArticleLikeEntity(null, userId, articleId, LocalDateTime.now(), null);
	}
}
