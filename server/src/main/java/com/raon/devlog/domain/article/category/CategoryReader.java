package com.raon.devlog.domain.article.category;

import org.springframework.stereotype.Component;

import com.raon.devlog.mapper.article.category.CategoryEntity;
import com.raon.devlog.mapper.article.category.CategoryMapper;

@Component
public class CategoryReader {
	private final CategoryMapper categoryMapper;

	public CategoryReader(CategoryMapper categoryMapper) {
		this.categoryMapper = categoryMapper;
	}

	public boolean exists(String name) {
		return categoryMapper.existsByName(name);
	}

	public CategoryEntity findBy(String name) {
		return categoryMapper.findBy(name);
	}
}
