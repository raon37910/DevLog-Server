package com.raon.devlog.mapper.article.tag;

import java.util.List;

public interface TagMapper {
	List<TagEntity> findByTagsIn(List<String> tags);

	void insertAll(List<TagEntity> tags);
}
