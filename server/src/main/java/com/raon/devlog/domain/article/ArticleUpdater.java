package com.raon.devlog.domain.article;

import org.springframework.stereotype.Component;

import com.raon.devlog.mapper.article.ArticleEntity;
import com.raon.devlog.mapper.article.ArticleMapper;

@Component
public class ArticleUpdater {
	private final ArticleMapper articleMapper;

	public ArticleUpdater(ArticleMapper articleMapper) {
		this.articleMapper = articleMapper;
	}

	public void update(ArticleEntity articleEntity) {
		articleMapper.update(articleEntity);
	}
}
