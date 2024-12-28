package com.raon.devlog.mapper.article.tag;

import java.time.LocalDateTime;

public record ArticleTagEntity(
	Long id,
	Long articleId,
	Long tagId,
	LocalDateTime createTime,
	LocalDateTime updateTime
) {
}
