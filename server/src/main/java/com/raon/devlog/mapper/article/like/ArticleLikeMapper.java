package com.raon.devlog.mapper.article.like;

import java.util.Optional;

public interface ArticleLikeMapper {
	Optional<ArticleLikeEntity> findByUserIdAndArticleId(Long userId, Long articleId);

	void create(ArticleLikeEntity articleLikeEntity);

	void delete(ArticleLikeEntity articleLikeEntity);
}
