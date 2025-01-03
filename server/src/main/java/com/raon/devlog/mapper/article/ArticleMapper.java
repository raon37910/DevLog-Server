package com.raon.devlog.mapper.article;

import java.util.List;
import java.util.Optional;

import com.raon.devlog.service.article.ArticleSearchParam;

public interface ArticleMapper {
	void append(ArticleEntity articleEntity);

	Long getLastInsertId();

	Optional<ArticleEntity> findById(Long articleId);

	void update(ArticleEntity articleEntity);

	void deleteById(Long articleId);

	List<ArticleSearchResult> getList(ArticleSearchParam params);
}
