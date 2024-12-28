package com.raon.devlog.domain.article.tag;

import java.util.List;

import org.springframework.stereotype.Component;

import com.raon.devlog.mapper.article.tag.ArticleTagMapper;
import com.raon.devlog.mapper.article.tag.TagEntity;
import com.raon.devlog.mapper.article.tag.TagMapper;

@Component
public class TagAppender {
	private final TagMapper tagMapper;
	private final ArticleTagMapper articleTagMapper;

	public TagAppender(TagMapper tagMapper, ArticleTagMapper articleTagMapper) {
		this.tagMapper = tagMapper;
		this.articleTagMapper = articleTagMapper;
	}

	public void appendAll(List<TagEntity> tags) {
		tagMapper.insertAll(tags);
	}

	public void appendArticleTags(Long articleId, List<TagEntity> tags) {
		articleTagMapper.insertAll(articleId, tags);
	}
}
