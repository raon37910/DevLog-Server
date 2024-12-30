package com.raon.devlog.repository.article.tag;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.raon.devlog.mapper.article.tag.TagEntity;
import com.raon.devlog.mapper.article.tag.TagMapper;

@Repository
public class TagQuery {
	private final TagMapper tagMapper;

	public TagQuery(TagMapper tagMapper) {
		this.tagMapper = tagMapper;
	}

	public List<TagEntity> findByTagsIn(List<String> tags) {
		return tagMapper.findByTagsIn(tags);
	}
}
