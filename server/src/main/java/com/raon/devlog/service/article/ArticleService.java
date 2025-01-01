package com.raon.devlog.service.article;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raon.devlog.mapper.article.ArticleEntity;
import com.raon.devlog.mapper.article.category.CategoryEntity;
import com.raon.devlog.mapper.article.tag.TagEntity;
import com.raon.devlog.repository.article.ArticleCommand;
import com.raon.devlog.repository.article.ArticleQuery;
import com.raon.devlog.repository.article.category.CategoryCommand;
import com.raon.devlog.repository.article.category.CategoryQuery;
import com.raon.devlog.repository.article.tag.TagCommand;
import com.raon.devlog.repository.article.tag.TagQuery;

@Service
public class ArticleService {

	private final CategoryQuery categoryQuery;
	private final CategoryCommand categoryCommand;
	private final TagQuery tagQuery;
	private final TagCommand tagCommand;
	private final ArticleQuery articleQuery;
	private final ArticleCommand articleCommand;

	public ArticleService(
		CategoryQuery categoryQuery,
		CategoryCommand categoryCommand,
		TagQuery tagQuery,
		TagCommand tagCommand,
		ArticleQuery articleQuery,
		ArticleCommand articleCommand
	) {
		this.categoryQuery = categoryQuery;
		this.categoryCommand = categoryCommand;
		this.tagQuery = tagQuery;
		this.tagCommand = tagCommand;
		this.articleQuery = articleQuery;
		this.articleCommand = articleCommand;
	}

	@Transactional
	public void createArticle(Article article, String category, List<String> tags) {
		categoryCommand.createCategoryIfNotExists(category);
		tagCommand.createTagsIfNotExists(tags);
		List<TagEntity> tagEntities = tagQuery.findByTagsIn(tags);
		CategoryEntity categoryEntity = categoryQuery.findBy(category);

		ArticleEntity articleEntity = article.toEntityWithCreateTime(categoryEntity.id(), LocalDateTime.now());

		articleCommand.append(articleEntity);
		tagCommand.appendArticleTags(articleQuery.getLastInsertId(), tagEntities);
	}

	@Transactional
	public void updateArticle(Long articleId, Article article, String category, List<String> tags) {
		categoryCommand.createCategoryIfNotExists(category);
		tagCommand.createTagsIfNotExists(tags);
		articleQuery.existsByIdOrElseThrow(articleId);

		CategoryEntity newCategoryEntity = categoryQuery.findBy(category);
		List<TagEntity> newTagEntities = tagQuery.findByTagsIn(tags);

		tagCommand.deleteAllByArticleId(articleId);
		tagCommand.appendArticleTags(articleId, newTagEntities);

		ArticleEntity updatedArticleEntity = article.toEntityWithUpdateTime(newCategoryEntity.id(),
			LocalDateTime.now());

		articleCommand.update(updatedArticleEntity);
	}

	@Transactional
	public void deleteArticle(Long articleId) {
		articleQuery.existsByIdOrElseThrow(articleId);
		articleCommand.deleteById(articleId);
	}
}
