package com.raon.devlog.repository.article.like;

import org.springframework.stereotype.Repository;

import com.raon.devlog.mapper.article.like.ArticleLikeEntity;
import com.raon.devlog.mapper.article.like.ArticleLikeMapper;

@Repository
public class ArticleLikeCommand {

	private final ArticleLikeMapper articleLikeMapper;

	public ArticleLikeCommand(ArticleLikeMapper articleLikeMapper) {
		this.articleLikeMapper = articleLikeMapper;
	}

	public void create(ArticleLikeEntity articleLikeEntity) {
		articleLikeMapper.create(articleLikeEntity);
	}

	public void delete(ArticleLikeEntity articleLikeEntity) {
		articleLikeMapper.delete(articleLikeEntity);
	}
}
