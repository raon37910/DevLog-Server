package com.raon.devlog.repository.article;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.raon.devlog.component.error.DevlogException;
import com.raon.devlog.component.error.ErrorType;
import com.raon.devlog.mapper.article.ArticleEntity;
import com.raon.devlog.mapper.article.ArticleMapper;

@Repository
public class ArticleQuery {
	private final ArticleMapper articleMapper;

	public ArticleQuery(ArticleMapper articleMapper) {
		this.articleMapper = articleMapper;
	}

	public Long getLastInsertId() {
		return this.articleMapper.getLastInsertId();
	}

	public Optional<ArticleEntity> findById(Long articleId) {
		return articleMapper.findById(articleId);
	}

	public void existsByIdOrElseThrow(Long articleId) {
		articleMapper.findById(articleId).orElseThrow(
			() -> new DevlogException(ErrorType.VALIDATION_ERROR)
		);
	}
}
