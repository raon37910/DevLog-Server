package com.raon.devlog.mapper.article.tag;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface ArticleTagMapper {
	void insertAll(@Param("articleId") Long articleId, @Param("tags") List<TagEntity> tags);

	void deleteAllByArticleId(Long articleId);
}
