package com.raon.devlog.repository.article.bookmark;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.raon.devlog.mapper.article.bookmark.ArticleBookMarkEntity;
import com.raon.devlog.mapper.article.bookmark.ArticleBookMarkMapper;

@Repository
public class ArticleBookMarkQuery {
	private final ArticleBookMarkMapper articleBookMarkMapper;

	public ArticleBookMarkQuery(ArticleBookMarkMapper articleBookMarkMapper) {
		this.articleBookMarkMapper = articleBookMarkMapper;
	}

	public Optional<ArticleBookMarkEntity> findByUserIdAndArticleId(Long userId, Long articleId) {
		return articleBookMarkMapper.findByUserIdAndArticleId(userId, articleId);
	}
}
