package com.raon.devlog.controller;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.raon.devlog.container.TestContainer;
import com.raon.devlog.service.auth.AuthService;
import com.raon.devlog.service.auth.model.SigninRequestInfo;
import com.raon.devlog.service.auth.model.Token;
import com.raon.devlog.service.user.CreateUserInfo;
import com.raon.devlog.service.user.UserService;

@Sql(scripts = "/setup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(TestContainer.class)
public class ArticleControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserService userService;

	@Autowired
	private AuthService authService;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	@DisplayName("게시글 생성 API - 성공")
	void articleCreateSuccess() throws Exception {
		String articleCreateRequest = """
			{
			  "title": "예제 제목",
			  "author": "작성자 이름",
			  "description": "이것은 예제 설명입니다.",
			  "link": "https://example.com",
			  "category": "기술",
			  "tags": [
			    "태그11",
			    "태그2",
			    "태그3"
			  ]
			}
			""";

		userService.createUser(new CreateUserInfo("admin@admin.com", "admin1234"));
		createAdminRole("admin@admin.com");
		Token token = authService.generateToken(new SigninRequestInfo("admin@admin.com", "admin1234"));

		mockMvc.perform(RestDocumentationRequestBuilders.post("/api/admin/articles")
				.content(articleCreateRequest).header("Authorization", "Bearer %s".formatted(token.accessToken()))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is2xxSuccessful())
			.andDo(MockMvcRestDocumentationWrapper.document("Article API", responseFields(
				fieldWithPath("result").type(JsonFieldType.STRING)
					.description("The result of the operation (e.g., SUCCESS)"),
				fieldWithPath("data").type(JsonFieldType.NULL).description("Null"),
				fieldWithPath("error").type(JsonFieldType.NULL).description("Error information (null if no error)"))));
	}

	@Test
	@DisplayName("게시글 생성 API 실패 - 로그인 하지 않은 유저")
	void articleCreateFailUnAuthorized() throws Exception {
		String articleCreateRequest = """
			{
			  "title": "예제 제목",
			  "author": "작성자 이름",
			  "description": "이것은 예제 설명입니다.",
			  "link": "https://example.com",
			  "category": "기술",
			  "tags": [
			    "태그11",
			    "태그2",
			    "태그3"
			  ]
			}
			""";

		mockMvc.perform(RestDocumentationRequestBuilders.post("/api/admin/articles")
				.content(articleCreateRequest)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized())
			.andDo(MockMvcRestDocumentationWrapper.document("Article API"));
	}

	@Test
	@DisplayName("게시글 생성 API 실패 - 어드민이 아닌 유저")
	void articleCreateFailForbidden() throws Exception {
		String articleCreateRequest = """
			{
			  "title": "예제 제목",
			  "author": "작성자 이름",
			  "description": "이것은 예제 설명입니다.",
			  "link": "https://example.com",
			  "category": "기술",
			  "tags": [
			    "태그11",
			    "태그2",
			    "태그3"
			  ]
			}
			""";

		userService.createUser(new CreateUserInfo("default@default.com", "default1234"));
		Token token = authService.generateToken(new SigninRequestInfo("default@default.com", "default1234"));

		mockMvc.perform(RestDocumentationRequestBuilders.post("/api/admin/articles")
				.content(articleCreateRequest).header("Authorization", "Bearer %s".formatted(token.accessToken()))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isForbidden())
			.andDo(MockMvcRestDocumentationWrapper.document("Article API"));
	}

	private void createAdminRole(String email) {
		String findUserQuery = "SELECT id FROM User WHERE EMAIL = ?";
		Long userId = jdbcTemplate.queryForObject(findUserQuery, Long.class, email);

		String adminRoleIdQuery = "SELECT id FROM Role WHERE NAME = 'ROLE_ADMIN'";
		Long adminRoleId = jdbcTemplate.queryForObject(adminRoleIdQuery, Long.class);

		String insertUserRoleQuery = "INSERT INTO UserRole (userId, roleId) VALUES (?, ?)";
		jdbcTemplate.update(insertUserRoleQuery, userId, adminRoleId);
	}
}
