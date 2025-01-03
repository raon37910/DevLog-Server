package com.raon.devlog.mapper.article.bookmark;

import java.util.Optional;

public interface ArticleBookMarkMapper {
	Optional<ArticleBookMarkEntity> findByUserIdAndArticleId(Long userId, Long articleId);

	void create(ArticleBookMarkEntity articleLikeEntity);
}
