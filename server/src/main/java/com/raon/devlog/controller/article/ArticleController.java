package com.raon.devlog.controller.article;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.raon.devlog.component.response.ApiResponse;
import com.raon.devlog.controller.article.request.ArticleCreateRequest;
import com.raon.devlog.controller.article.response.ArticleResponse;
import com.raon.devlog.service.article.ArticleSearchParam;
import com.raon.devlog.service.article.ArticleService;
import com.raon.devlog.service.article.bookmark.ArticleBookMarkService;
import com.raon.devlog.service.article.like.ArticleLikeService;

import jakarta.validation.Valid;

@RestController
public class ArticleController {

	private final ArticleService articleService;
	private final ArticleLikeService articleLikeService;
	private final ArticleBookMarkService articleBookMarkService;

	public ArticleController(
		ArticleService articleService,
		ArticleLikeService articleLikeService,
		ArticleBookMarkService articleBookMarkService) {
		this.articleService = articleService;
		this.articleLikeService = articleLikeService;
		this.articleBookMarkService = articleBookMarkService;
	}

	@GetMapping("/api/articles")
	public ApiResponse<List<ArticleResponse>> getArticles(
		@RequestParam(required = false) String tag,
		@RequestParam(required = false) String category,
		@RequestParam(required = false, defaultValue = "false") boolean bookmarked,
		@PageableDefault Pageable pageable
	) {
		String email = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ArticleSearchParam articleSearchParam = new ArticleSearchParam(tag, category, bookmarked, email, pageable);
		List<ArticleResponse> articleSearchResults = articleService.getArticleList(articleSearchParam)
			.stream()
			.map(ArticleResponse::from)
			.toList();

		return ApiResponse.success(articleSearchResults);
	}

	@PostMapping("/api/admin/articles")
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse<Void> createArticle(@Valid @RequestBody ArticleCreateRequest request) {
		articleService.createArticle(request.toArticle(), request.category(), request.tags());

		return ApiResponse.success(null);
	}

	@PutMapping("/api/admin/articles/{articleId}")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse<Void> updateArticle(@PathVariable Long articleId,
		@Valid @RequestBody ArticleCreateRequest request) {
		articleService.updateArticle(articleId, request.toArticle(), request.category(), request.tags());

		return ApiResponse.success(null);
	}

	@DeleteMapping("/api/admin/articles/{articleId}")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse<Void> deleteArticle(@PathVariable Long articleId) {
		articleService.deleteArticle(articleId);

		return ApiResponse.success(null);
	}

	@PostMapping("/api/articles/{articleId}/like")
	public ApiResponse<Void> likeArticle(@PathVariable Long articleId) {
		String email = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		articleLikeService.like(articleId, email);

		return ApiResponse.success(null);
	}

	@DeleteMapping("/api/articles/{articleId}/like")
	public ApiResponse<Void> cancelLikeArticle(@PathVariable Long articleId) {
		String email = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		articleLikeService.cancelLike(articleId, email);

		return ApiResponse.success(null);
	}

	@PostMapping("/api/articles/{articleId}/bookMark")
	public ApiResponse<Void> bookMarkArticle(@PathVariable Long articleId) {
		String email = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		articleBookMarkService.bookmark(articleId, email);

		return ApiResponse.success(null);
	}

	@DeleteMapping("/api/articles/{articleId}/bookMark")
	public ApiResponse<Void> cancelBookMarkArticle(@PathVariable Long articleId) {
		String email = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		articleBookMarkService.cancelBookmark(articleId, email);

		return ApiResponse.success(null);
	}
}
