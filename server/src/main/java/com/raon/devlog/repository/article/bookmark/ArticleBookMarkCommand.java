package com.raon.devlog.repository.article.bookmark;

import org.springframework.stereotype.Repository;

import com.raon.devlog.mapper.article.bookmark.ArticleBookMarkEntity;
import com.raon.devlog.mapper.article.bookmark.ArticleBookMarkMapper;

@Repository
public class ArticleBookMarkCommand {

	private final ArticleBookMarkMapper articleBookMarkMapper;

	public ArticleBookMarkCommand(ArticleBookMarkMapper articleBookMarkMapper) {
		this.articleBookMarkMapper = articleBookMarkMapper;
	}

	public void create(ArticleBookMarkEntity articleBookMarkEntity) {
		articleBookMarkMapper.create(articleBookMarkEntity);
	}

	public void delete(ArticleBookMarkEntity articleBookMarkEntity) {
		articleBookMarkMapper.delete(articleBookMarkEntity);
	}
}
