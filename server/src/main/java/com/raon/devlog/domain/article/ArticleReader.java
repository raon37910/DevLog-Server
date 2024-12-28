package com.raon.devlog.domain.article;

import org.springframework.stereotype.Component;

import com.raon.devlog.mapper.article.ArticleMapper;

@Component
public class ArticleReader {
	private final ArticleMapper articleMapper;

	public ArticleReader(ArticleMapper articleMapper) {
		this.articleMapper = articleMapper;
	}

	public Long getLastInsertId() {
		return this.articleMapper.getLastInsertId();
	}
}
