package com.raon.devlog.mapper.article.category;

public interface CategoryMapper {
	void insert(CategoryEntity categoryEntity);

	boolean existsByName(String name);

	CategoryEntity findBy(String name);
}
