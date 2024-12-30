package com.raon.devlog.repository.article.category;

import org.springframework.stereotype.Repository;

import com.raon.devlog.mapper.article.category.CategoryEntity;
import com.raon.devlog.mapper.article.category.CategoryMapper;

@Repository
public class CategoryCommand {
	private final CategoryMapper categoryMapper;

	public CategoryCommand(CategoryMapper categoryMapper) {
		this.categoryMapper = categoryMapper;
	}

	public void append(CategoryEntity category) {
		categoryMapper.insert(category);
	}

	public void createCategoryIfNotExists(String category) {
		if (!categoryMapper.existsByName(category)) {
			append(CategoryEntity.from(category));
		}
	}
}
