package com.raon.devlog.mapper.article;

import java.util.Optional;

public interface ArticleMapper {
	void append(ArticleEntity articleEntity);

	Long getLastInsertId();

	Optional<ArticleEntity> findById(Long articleId);

	void update(ArticleEntity articleEntity);

	void deleteById(Long articleId);
}
