package com.raon.devlog.repository.article.tag;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.raon.devlog.mapper.article.tag.ArticleTagMapper;
import com.raon.devlog.mapper.article.tag.TagEntity;
import com.raon.devlog.mapper.article.tag.TagMapper;

@Repository
public class TagCommand {
	private final TagMapper tagMapper;
	private final ArticleTagMapper articleTagMapper;

	public TagCommand(TagMapper tagMapper, ArticleTagMapper articleTagMapper) {
		this.tagMapper = tagMapper;
		this.articleTagMapper = articleTagMapper;
	}

	public void appendAll(List<TagEntity> tags) {
		tagMapper.insertAll(tags);
	}

	public void appendArticleTags(Long articleId, List<TagEntity> tags) {
		articleTagMapper.insertAll(articleId, tags);
	}

	public void deleteAllByArticleId(Long articleId) {
		articleTagMapper.deleteAllByArticleId(articleId);
	}

	public void createTagsIfNotExists(List<String> tags) {
		List<String> foundTagNames = tagMapper.findByTagsIn(tags).stream()
			.map(TagEntity::name)
			.toList();

		List<TagEntity> newTagNames = tags.stream()
			.filter(tag -> !foundTagNames.contains(tag))
			.map(TagEntity::from)
			.toList();

		if (!newTagNames.isEmpty()) {
			appendAll(newTagNames);
		}
	}
}
