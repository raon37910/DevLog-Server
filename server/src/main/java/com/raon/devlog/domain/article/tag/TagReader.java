package com.raon.devlog.domain.article.tag;

import java.util.List;

import org.springframework.stereotype.Component;

import com.raon.devlog.mapper.article.tag.TagEntity;
import com.raon.devlog.mapper.article.tag.TagMapper;

@Component
public class TagReader {

	private final TagMapper tagMapper;

	public TagReader(TagMapper tagMapper) {
		this.tagMapper = tagMapper;
	}

	public List<TagEntity> findByTagsIn(List<String> tags) {
		return tagMapper.findByTagsIn(tags);
	}
}
