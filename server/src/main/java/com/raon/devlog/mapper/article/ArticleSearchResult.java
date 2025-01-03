package com.raon.devlog.mapper.article;

import java.time.LocalDateTime;

public record ArticleSearchResult(
	Long id,
	String title,
	String author,
	String description,
	String link,
	Long views,
	LocalDateTime createTime,
	LocalDateTime updateTime,
	String categoryName,
	String tags,
	boolean isLiked
) {
}
