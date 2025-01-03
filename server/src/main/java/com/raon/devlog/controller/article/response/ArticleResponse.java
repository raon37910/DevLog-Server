package com.raon.devlog.controller.article.response;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.raon.devlog.mapper.article.ArticleSearchResult;

public record ArticleResponse(
	Long id,
	String title,
	String author,
	String description,
	String link,
	Long views,
	String category,
	List<String> tags,
	boolean liked,
	LocalDateTime createTime,
	LocalDateTime updateTime
) {

	public static ArticleResponse from(ArticleSearchResult articleSearchResult) {
		return new ArticleResponse(
			articleSearchResult.id(),
			articleSearchResult.title(),
			articleSearchResult.author(),
			articleSearchResult.description(),
			articleSearchResult.link(),
			articleSearchResult.views(),
			articleSearchResult.categoryName(),
			articleSearchResult.tags() != null ? Arrays.asList(articleSearchResult.tags().split(",")) : List.of(),
			articleSearchResult.isLiked(),
			articleSearchResult.createTime(),
			articleSearchResult.updateTime()
		);
	}
}
