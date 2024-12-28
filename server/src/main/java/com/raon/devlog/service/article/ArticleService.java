package com.raon.devlog.service.article;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raon.devlog.domain.article.ArticleAppender;
import com.raon.devlog.domain.article.ArticleReader;
import com.raon.devlog.domain.article.category.CategoryReader;
import com.raon.devlog.domain.article.tag.TagAppender;
import com.raon.devlog.domain.article.tag.TagReader;
import com.raon.devlog.mapper.article.ArticleEntity;
import com.raon.devlog.mapper.article.category.CategoryEntity;
import com.raon.devlog.mapper.article.tag.TagEntity;

@Service
public class ArticleService {

	private final CategoryReader categoryReader;
	private final TagReader tagReader;
	private final TagAppender tagAppender;
	private final ArticleAppender articleAppender;
	private final ArticleReader articleReader;

	public ArticleService(
		CategoryReader categoryReader,
		TagReader tagReader,
		TagAppender tagAppender,
		ArticleAppender articleAppender,
		ArticleReader articleReader) {
		this.categoryReader = categoryReader;
		this.tagReader = tagReader;
		this.tagAppender = tagAppender;
		this.articleAppender = articleAppender;
		this.articleReader = articleReader;
	}

	@Transactional
	public void createArticle(Article article, String category, List<String> tags) {
		List<TagEntity> tagEntities = tagReader.findByTagsIn(tags);
		CategoryEntity categoryEntity = categoryReader.findBy(category);
		ArticleEntity articleEntity = new ArticleEntity(
			null,
			article.title(),
			article.author(),
			article.description(),
			article.link(),
			0L,
			categoryEntity.id(),
			LocalDateTime.now(),
			null
		);

		articleAppender.append(articleEntity);
		tagAppender.appendArticleTags(articleReader.getLastInsertId(), tagEntities);
	}
}
