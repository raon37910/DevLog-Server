package com.raon.devlog.controller.article.request;

import java.time.LocalDateTime;
import java.util.List;

import com.raon.devlog.service.article.Article;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ArticleUpdateRequest(
	@NotBlank(message = "제목은 필수 입력 값입니다.")
	@Size(max = 100, message = "제목은 최대 100자까지 입력 가능합니다.")
	String title,

	@NotBlank(message = "작성자는 필수 입력 값입니다.")
	@Size(max = 100, message = "작성자는 최대 100자까지 입력 가능합니다.")
	String author,

	// 설명: 필수 입력, 최대 100자
	@NotBlank(message = "설명은 필수 입력 값입니다.")
	@Size(max = 100, message = "설명은 최대 100자까지 입력 가능합니다.")
	String description,

	// 링크: 필수 입력, URL 형식 검증
	@NotBlank(message = "링크는 필수 입력 값입니다.")
	@Pattern(
		regexp = "^(http|https)://.*$",
		message = "링크는 유효한 URL 형식이어야 합니다. (http 또는 https로 시작)"
	)
	@Size(max = 200, message = "설명은 최대 100자까지 입력 가능합니다.")
	String link,

	@NotBlank(message = "카테고리는 필수 입력 값입니다.")
	@Size(max = 30, message = "카테고리는 최대 30자까지 입력 가능합니다.")
	String category,

	@NotNull(message = "태그 리스트는 null이 될 수 없습니다.")
	@Size(max = 3, message = "태그는 최대 3개까지 입력 가능합니다.")
	List<@Size(max = 20, message = "각 태그는 최대 20자까지 입력 가능합니다.") String> tags
) {

	public Article toArticle() {
		return new Article(null, title, description, link, author, 0L, LocalDateTime.now(), null);
	}
}
