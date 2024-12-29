package com.raon.devlog.domain.article;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.raon.devlog.mapper.article.ArticleEntity;
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

	public Optional<ArticleEntity> findById(Long articleId) {
		return articleMapper.findById(articleId);
	}
}
