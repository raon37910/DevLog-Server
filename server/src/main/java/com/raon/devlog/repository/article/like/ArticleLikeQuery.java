package com.raon.devlog.repository.article.like;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.raon.devlog.mapper.article.like.ArticleLikeEntity;
import com.raon.devlog.mapper.article.like.ArticleLikeMapper;

@Repository
public class ArticleLikeQuery {
	private final ArticleLikeMapper articleLikeMapper;

	public ArticleLikeQuery(ArticleLikeMapper articleLikeMapper) {
		this.articleLikeMapper = articleLikeMapper;
	}

	public Optional<ArticleLikeEntity> findByUserIdAndArticleId(Long userId, Long articleId) {
		return articleLikeMapper.findByUserIdAndArticleId(userId, articleId);
	}
}
