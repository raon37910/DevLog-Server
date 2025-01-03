package com.raon.devlog.mapper.article.category;

import java.util.List;

public interface CategoryMapper {
	void insert(CategoryEntity categoryEntity);

	boolean existsByName(String name);

	CategoryEntity findBy(String name);

	CategoryEntity findById(Long id);
	
	List<CategoryEntity> findAll();
}
