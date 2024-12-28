package com.raon.devlog.service.article.category;

import org.springframework.stereotype.Service;

import com.raon.devlog.domain.article.category.CategoryAppender;
import com.raon.devlog.domain.article.category.CategoryReader;
import com.raon.devlog.mapper.article.category.CategoryEntity;

@Service
public class CategoryService {

	private final CategoryAppender categoryAppender;
	private final CategoryReader categoryReader;

	public CategoryService(CategoryAppender categoryAppender, CategoryReader categoryReader) {
		this.categoryAppender = categoryAppender;
		this.categoryReader = categoryReader;
	}

	public void createCategoryIfNotExists(String categoryName) {
		if (!categoryReader.exists(categoryName)) {
			categoryAppender.append(CategoryEntity.from(categoryName));
		}
	}
}
