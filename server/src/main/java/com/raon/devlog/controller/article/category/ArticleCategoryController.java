package com.raon.devlog.controller.article.category;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raon.devlog.component.response.ApiResponse;
import com.raon.devlog.controller.article.category.response.CategoryResponse;
import com.raon.devlog.service.article.category.ArticleCategoryService;

@RestController
public class ArticleCategoryController {

	private final ArticleCategoryService articleCategoryService;

	public ArticleCategoryController(ArticleCategoryService articleCategoryService) {
		this.articleCategoryService = articleCategoryService;
	}

	@GetMapping("/api/category")
	public ApiResponse<List<CategoryResponse>> getArticleCategories() {
		List<CategoryResponse> categoryResponses = articleCategoryService.findAll()
			.stream()
			.map(categoryEntity -> new CategoryResponse(categoryEntity.id(), categoryEntity.name()))
			.toList();

		return ApiResponse.success(categoryResponses);
	}
}
