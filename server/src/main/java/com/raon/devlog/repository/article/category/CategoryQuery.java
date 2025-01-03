package com.raon.devlog.repository.article.category;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.raon.devlog.mapper.article.category.CategoryEntity;
import com.raon.devlog.mapper.article.category.CategoryMapper;

@Repository
public class CategoryQuery {

	private final CategoryMapper categoryMapper;

	public CategoryQuery(CategoryMapper categoryMapper) {
		this.categoryMapper = categoryMapper;
	}

	public boolean exists(String name) {
		return categoryMapper.existsByName(name);
	}

	public CategoryEntity findBy(String name) {
		return categoryMapper.findBy(name);
	}

	public List<CategoryEntity> findAll() {
		return categoryMapper.findAll();
	}
}
