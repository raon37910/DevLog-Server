package com.raon.devlog.repository.article;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.raon.devlog.component.error.DevlogException;
import com.raon.devlog.component.error.ErrorType;
import com.raon.devlog.mapper.article.ArticleEntity;
import com.raon.devlog.mapper.article.ArticleMapper;
import com.raon.devlog.mapper.article.ArticleSearchResult;
import com.raon.devlog.service.article.ArticleSearchParam;

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

	public List<ArticleSearchResult> getList(ArticleSearchParam param) {
		return articleMapper.getList(param);
	}
}
