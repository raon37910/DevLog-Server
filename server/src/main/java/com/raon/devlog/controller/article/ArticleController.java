package com.raon.devlog.controller.article;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.raon.devlog.component.response.ApiResponse;
import com.raon.devlog.controller.article.request.ArticleCreateRequest;
import com.raon.devlog.service.article.ArticleService;
import com.raon.devlog.service.article.category.CategoryService;
import com.raon.devlog.service.article.tag.TagService;

import jakarta.validation.Valid;

@RestController
public class ArticleController {

	private final ArticleService articleService;
	private final TagService tagService;
	private final CategoryService categoryService;

	public ArticleController(ArticleService articleService, TagService tagService, CategoryService categoryService) {
		this.articleService = articleService;
		this.tagService = tagService;
		this.categoryService = categoryService;
	}

	@PostMapping("/api/admin/articles")
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse<Void> createArticle(@Valid @RequestBody ArticleCreateRequest request) {
		categoryService.createCategoryIfNotExists(request.category());
		tagService.createTagsIfNotExists(request.tags());
		articleService.createArticle(request.toArticle(), request.category(), request.tags());

		return ApiResponse.success(null);
	}

	@PutMapping("/api/admin/articles/{articleId}")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse<Void> updateArticle(@PathVariable Long articleId,
		@Valid @RequestBody ArticleCreateRequest request) {
		categoryService.createCategoryIfNotExists(request.category());
		tagService.createTagsIfNotExists(request.tags());
		articleService.updateArticle(articleId, request.toArticle(), request.category(), request.tags());

		return ApiResponse.success(null);
	}
}
