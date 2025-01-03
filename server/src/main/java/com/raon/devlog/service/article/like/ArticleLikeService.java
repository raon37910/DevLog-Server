package com.raon.devlog.service.article.like;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raon.devlog.component.error.DevlogException;
import com.raon.devlog.component.error.ErrorType;
import com.raon.devlog.mapper.article.ArticleEntity;
import com.raon.devlog.mapper.article.like.ArticleLikeEntity;
import com.raon.devlog.mapper.user.UserEntity;
import com.raon.devlog.repository.article.ArticleQuery;
import com.raon.devlog.repository.article.like.ArticleLikeCommand;
import com.raon.devlog.repository.article.like.ArticleLikeQuery;
import com.raon.devlog.repository.user.UserQuery;

@Service
public class ArticleLikeService {

	private final ArticleLikeQuery articleLikeQuery;
	private final ArticleLikeCommand articleLikeCommand;
	private final ArticleQuery articleQuery;
	private final UserQuery userQuery;

	public ArticleLikeService(
		ArticleLikeQuery articleLikeQuery,
		ArticleLikeCommand articleLikeCommand,
		ArticleQuery articleQuery,
		UserQuery userQuery
	) {
		this.articleLikeQuery = articleLikeQuery;
		this.articleLikeCommand = articleLikeCommand;
		this.articleQuery = articleQuery;
		this.userQuery = userQuery;
	}

	@Transactional
	public void like(Long articleId, String email) {
		UserEntity user = userQuery.findByEmail(email).orElseThrow(
			() -> new DevlogException(ErrorType.VALIDATION_ERROR, "존재 하지 않는 회원입니다.")
		);

		ArticleEntity article = articleQuery.findById(articleId).orElseThrow(
			() -> new DevlogException(ErrorType.VALIDATION_ERROR, "존재 하지 않는 게시글입니다.")
		);

		articleLikeQuery.findByUserIdAndArticleId(user.id(), article.id())
			.ifPresent(articleLikeEntity -> {
				throw new DevlogException(ErrorType.VALIDATION_ERROR, "이미 좋아요 한 게시물입니다.");
			});

		articleLikeCommand.create(ArticleLikeEntity.from(user.id(), article.id()));
	}
}
