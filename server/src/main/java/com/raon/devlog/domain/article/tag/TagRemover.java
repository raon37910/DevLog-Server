package com.raon.devlog.domain.article.tag;

import org.springframework.stereotype.Component;

import com.raon.devlog.mapper.article.tag.ArticleTagMapper;

@Component
public class TagRemover {
	private final ArticleTagMapper articleTagMapper;

	public TagRemover(ArticleTagMapper tagMapper) {
		this.articleTagMapper = tagMapper;
	}

	public void deleteAllByArticleId(Long articleId) {
		articleTagMapper.deleteAllByArticleId(articleId);
	}
}
