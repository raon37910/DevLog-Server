package com.raon.devlog.repository.article;

import org.springframework.stereotype.Repository;

import com.raon.devlog.mapper.article.ArticleEntity;
import com.raon.devlog.mapper.article.ArticleMapper;

@Repository
public class ArticleCommand {
	private final ArticleMapper articleMapper;

	public ArticleCommand(ArticleMapper articleMapper) {
		this.articleMapper = articleMapper;
	}

	public void append(ArticleEntity articleEntity) {
		articleMapper.append(articleEntity);
	}

	public void update(ArticleEntity articleEntity) {
		articleMapper.update(articleEntity);
	}

	public void deleteById(Long articleId) {
		articleMapper.deleteById(articleId);
	}
}
