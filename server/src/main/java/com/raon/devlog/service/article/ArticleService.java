package com.raon.devlog.service.article;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raon.devlog.component.error.DevlogException;
import com.raon.devlog.component.error.ErrorType;
import com.raon.devlog.domain.article.ArticleAppender;
import com.raon.devlog.domain.article.ArticleReader;
import com.raon.devlog.domain.article.ArticleUpdater;
import com.raon.devlog.domain.article.category.CategoryReader;
import com.raon.devlog.domain.article.tag.TagAppender;
import com.raon.devlog.domain.article.tag.TagReader;
import com.raon.devlog.domain.article.tag.TagRemover;
import com.raon.devlog.mapper.article.ArticleEntity;
import com.raon.devlog.mapper.article.category.CategoryEntity;
import com.raon.devlog.mapper.article.tag.TagEntity;

// FIXME 도메인을 너무 많이 쪼갰음 Query Command 기준으로 쪼개는개 좋을 듯
@Service
public class ArticleService {

	private final CategoryReader categoryReader;
	private final TagReader tagReader;
	private final TagAppender tagAppender;
	private final TagRemover tagRemover;
	private final ArticleAppender articleAppender;
	private final ArticleReader articleReader;
	private final ArticleUpdater articleUpdater;

	public ArticleService(
		CategoryReader categoryReader,
		TagReader tagReader,
		TagAppender tagAppender,
		TagRemover tagRemover,
		ArticleAppender articleAppender,
		ArticleReader articleReader,
		ArticleUpdater articleUpdater) {
		this.categoryReader = categoryReader;
		this.tagReader = tagReader;
		this.tagAppender = tagAppender;
		this.tagRemover = tagRemover;
		this.articleAppender = articleAppender;
		this.articleReader = articleReader;
		this.articleUpdater = articleUpdater;
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

	@Transactional
	public void updateArticle(Long articleId, Article article, String category, List<String> tags) {
		ArticleEntity articleEntity = articleReader.findById(articleId)
			.orElseThrow(() -> new DevlogException(ErrorType.VALIDATION_ERROR));

		CategoryEntity newCategoryEntity = categoryReader.findBy(category);
		List<TagEntity> newTagEntities = tagReader.findByTagsIn(tags);

		tagRemover.deleteAllByArticleId(articleId);
		tagAppender.appendArticleTags(articleId, newTagEntities);

		ArticleEntity updatedArticleEntity = new ArticleEntity(
			articleId,
			article.title(),
			article.author(),
			article.description(),
			article.link(),
			article.views(),
			newCategoryEntity.id(),
			articleEntity.createTime(),
			LocalDateTime.now()
		);
		articleUpdater.update(updatedArticleEntity);
	}
}
