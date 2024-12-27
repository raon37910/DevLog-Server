package com.raon.devlog.controller.article;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raon.devlog.support.response.ApiResponse;

@RestController
public class ArticleController {

	@PostMapping("/api/admin/articles")
	public ApiResponse<Void> createArticle() {
		return null;
	}
}
