package com.raon.devlog.service.article;

import org.springframework.data.domain.Pageable;

public record ArticleSearchParam(
	String tag,
	String category,
	boolean bookMarked,
	String email,
	Pageable pageable
) {
}
