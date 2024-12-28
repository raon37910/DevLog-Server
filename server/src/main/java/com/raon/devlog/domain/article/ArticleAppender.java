package com.raon.devlog.domain.article;

import org.springframework.stereotype.Component;

import com.raon.devlog.mapper.article.ArticleEntity;
import com.raon.devlog.mapper.article.ArticleMapper;

@Component
public class ArticleAppender {

	private final ArticleMapper articleMapper;

	public ArticleAppender(ArticleMapper articleMapper) {
		this.articleMapper = articleMapper;
	}

	public void append(ArticleEntity articleEntity) {
		articleMapper.append(articleEntity);
	}
}
