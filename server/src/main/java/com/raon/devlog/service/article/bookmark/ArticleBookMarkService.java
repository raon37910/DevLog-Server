package com.raon.devlog.service.article.bookmark;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raon.devlog.component.error.DevlogException;
import com.raon.devlog.component.error.ErrorType;
import com.raon.devlog.mapper.article.ArticleEntity;
import com.raon.devlog.mapper.article.bookmark.ArticleBookMarkEntity;
import com.raon.devlog.mapper.user.UserEntity;
import com.raon.devlog.repository.article.ArticleQuery;
import com.raon.devlog.repository.article.bookmark.ArticleBookMarkCommand;
import com.raon.devlog.repository.article.bookmark.ArticleBookMarkQuery;
import com.raon.devlog.repository.user.UserQuery;

@Service
public class ArticleBookMarkService {

	private final ArticleBookMarkQuery articleBookMarkQuery;
	private final ArticleBookMarkCommand articleBookMarkCommand;
	private final UserQuery userQuery;
	private final ArticleQuery articleQuery;

	public ArticleBookMarkService(ArticleBookMarkQuery articleBookMarkQuery,
		ArticleBookMarkCommand articleBookMarkCommand,
		UserQuery userQuery, ArticleQuery articleQuery) {
		this.articleBookMarkQuery = articleBookMarkQuery;
		this.articleBookMarkCommand = articleBookMarkCommand;
		this.userQuery = userQuery;
		this.articleQuery = articleQuery;
	}

	@Transactional
	public void bookmark(Long articleId, String email) {
		UserEntity user = userQuery.findByEmail(email).orElseThrow(
			() -> new DevlogException(ErrorType.VALIDATION_ERROR, "존재 하지 않는 회원입니다."));

		ArticleEntity article = articleQuery.findById(articleId).orElseThrow(
			() -> new DevlogException(ErrorType.VALIDATION_ERROR, "존재 하지 않는 게시글입니다."));

		articleBookMarkQuery.findByUserIdAndArticleId(user.id(), article.id())
			.ifPresent(articleLikeEntity -> {
				throw new DevlogException(ErrorType.VALIDATION_ERROR, "이미 북마크 한 게시물입니다.");
			});

		articleBookMarkCommand.create(ArticleBookMarkEntity.from(user.id(), article.id()));
	}

	@Transactional
	public void cancelBookmark(Long articleId, String email) {
		UserEntity user = userQuery.findByEmail(email).orElseThrow(
			() -> new DevlogException(ErrorType.VALIDATION_ERROR, "존재 하지 않는 회원입니다."));

		ArticleEntity article = articleQuery.findById(articleId).orElseThrow(
			() -> new DevlogException(ErrorType.VALIDATION_ERROR, "존재 하지 않는 게시글입니다."));

		ArticleBookMarkEntity bookMark = articleBookMarkQuery.findByUserIdAndArticleId(user.id(),
				article.id())
			.orElseThrow(() -> new DevlogException(ErrorType.VALIDATION_ERROR, "북마크 정보가 존재하지 않습니다."));

		articleBookMarkCommand.delete(bookMark);
	}
}
