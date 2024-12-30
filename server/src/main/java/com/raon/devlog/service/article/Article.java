package com.raon.devlog.service.article;

import java.time.LocalDateTime;

import com.raon.devlog.mapper.article.ArticleEntity;

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

	public ArticleEntity toEntity() {
		return new ArticleEntity(id, title, author, description, link, views, null, createTime, updateTime);
	}

	public ArticleEntity toEntityWith(Long categoryId) {
		return new ArticleEntity(id, title, author, description, link, views, categoryId, createTime, updateTime);
	}

	public ArticleEntity toEntityWithCreateTime(Long categoryId, LocalDateTime createTime) {
		return new ArticleEntity(id, title, author, description, link, views, categoryId, createTime, updateTime);
	}

	public ArticleEntity toEntityWithUpdateTime(Long categoryId, LocalDateTime updateTime) {
		return new ArticleEntity(id, title, author, description, link, views, categoryId, createTime, updateTime);
	}
}
