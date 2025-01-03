package com.raon.devlog.service.article.category;

import java.util.List;

import org.springframework.stereotype.Service;

import com.raon.devlog.mapper.article.category.CategoryEntity;
import com.raon.devlog.repository.article.category.CategoryQuery;

@Service
public class ArticleCategoryService {

	private final CategoryQuery categoryQuery;

	public ArticleCategoryService(CategoryQuery categoryQuery) {
		this.categoryQuery = categoryQuery;
	}

	public List<CategoryEntity> findAll() {
		return categoryQuery.findAll();
	}
}
