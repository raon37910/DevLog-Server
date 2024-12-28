package com.raon.devlog.mapper.article;

public interface ArticleMapper {
	void append(ArticleEntity articleEntity);

	Long getLastInsertId();
}
