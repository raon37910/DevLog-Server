package com.raon.devlog.service.article.tag;

import java.util.List;

import org.springframework.stereotype.Service;

import com.raon.devlog.domain.article.tag.TagAppender;
import com.raon.devlog.domain.article.tag.TagReader;
import com.raon.devlog.mapper.article.tag.TagEntity;

@Service
public class TagService {

	private final TagReader tagReader;
	private final TagAppender tagAppender;

	public TagService(TagReader tagReader, TagAppender tagAppender) {
		this.tagReader = tagReader;
		this.tagAppender = tagAppender;
	}

	public void createTagsIfNotExists(List<String> tags) {
		List<String> foundTagNames = tagReader.findByTagsIn(tags).stream()
			.map(TagEntity::name)
			.toList();

		List<TagEntity> newTagNames = tags.stream()
			.filter(tag -> !foundTagNames.contains(tag))
			.map(TagEntity::from)
			.toList();

		if (!newTagNames.isEmpty()) {
			tagAppender.appendAll(newTagNames);
		}
	}
}
