package com.raon.devlog.domain.article.category;

import org.springframework.stereotype.Component;

import com.raon.devlog.mapper.article.category.CategoryEntity;
import com.raon.devlog.mapper.article.category.CategoryMapper;

@Component
public class CategoryAppender {

	private final CategoryMapper categoryMapper;

	public CategoryAppender(CategoryMapper categoryMapper) {
		this.categoryMapper = categoryMapper;
	}

	public void append(CategoryEntity category) {
		categoryMapper.insert(category);
	}
}
