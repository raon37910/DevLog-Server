package com.raon.devlog.controller;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.web.servlet.MockMvc;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.raon.devlog.container.TestContainer;
import com.raon.devlog.service.user.CreateUserInfo;
import com.raon.devlog.service.user.UserService;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(TestContainer.class)
public class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserService userService;

	@BeforeAll
	static void setupTable(@Autowired JdbcTemplate jdbcTemplate) {
		// 테이블 생성 SQL 실행
		String createUserTable = """
							CREATE TABLE IF NOT EXISTS `User`
							(
								`id`              INT PRIMARY KEY AUTO_INCREMENT,
								`email`           VARCHAR(45),
								`password`        VARCHAR(200),
								`name`            VARCHAR(100),
								`description`     VARCHAR(100),
								`profileImageUrl` VARCHAR(200),
								`createTime`      DATETIME,
								`updateTime`      DATETIME
							);
			""";

		String createRoleTable = """
				CREATE TABLE IF NOT EXISTS `Role`
			 (
			     `id`          INT PRIMARY KEY AUTO_INCREMENT,
			     `name`        VARCHAR(45),
			     `description` VARCHAR(100),
			     `createTime`  DATETIME,
			     `updateTime`  DATETIME
			 );
			""";

		String createUserRoleTable = """
				CREATE TABLE IF NOT EXISTS `UserRole`
			 (
			     `id`         INT PRIMARY KEY AUTO_INCREMENT,
			     `userId`     INT,
			     `roleId`     INT,
			     `createTime` DATETIME,
			     `updateTime` DATETIME
			 );
			""";

		jdbcTemplate.execute(createUserTable);
		jdbcTemplate.execute(createRoleTable);
		jdbcTemplate.execute(createUserRoleTable);
	}

	@BeforeEach
	void defaultDataSetup(@Autowired JdbcTemplate jdbcTemplate) {
		String defaultRoleSql = """
				INSERT INTO Role (name, description, createTime, updateTime)
				VALUES
					('ROLE_ADMIN', '어드민', NOW(), NOW()),
					('ROLE_USER', '일반유저', NOW(), NOW());
			""";

		jdbcTemplate.execute(defaultRoleSql);
	}

	@AfterEach
	void cleanup(@Autowired JdbcTemplate jdbcTemplate) {
		jdbcTemplate.execute("DELETE FROM UserRole");
		jdbcTemplate.execute("DELETE FROM Role");
		jdbcTemplate.execute("DELETE FROM User");
	}

	@Test
	@DisplayName("로그인 API - 성공")
	void signinSuccess() throws Exception {
		String signinRequest = """
			{
				"email": "test@test.com",
				"password": "qwer1234"
			}
			""";
		userService.createUser(new CreateUserInfo("test@test.com", "qwer1234"));

		mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auth/token")
				.content(signinRequest)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is2xxSuccessful())
			.andDo(MockMvcRestDocumentationWrapper.document("AUTH API", responseFields(
				fieldWithPath("result").type(JsonFieldType.STRING)
					.description("The result of the operation (e.g., SUCCESS)"),
				fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("AccessToken"),
				fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("RefreshToken"),
				fieldWithPath("error").type(JsonFieldType.NULL).description("Error information (null if no error)"))));
	}

	@Test
	@DisplayName("로그인 API 실패 - 비밀번호 불일치")
	void signinFailWhenPasswordMismatch() throws Exception {
		String signinRequest = """
			{
				"email": "test@test.com",
				"password": "qwer12345"
			}
			""";

		userService.createUser(new CreateUserInfo("test@test.com", "qwer1234"));

		mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auth/token")
				.content(signinRequest)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized())
			.andDo(MockMvcRestDocumentationWrapper.document("AUTH API", responseFields(
						fieldWithPath("result").type(JsonFieldType.STRING)
							.description("The result of the operation (e.g., SUCCESS)"),
						fieldWithPath("data").type(JsonFieldType.NULL).description("data info null"),
						fieldWithPath("error.code").type(JsonFieldType.STRING).description("Error Code"),
						fieldWithPath("error.message").type(JsonFieldType.STRING).description("Error Message"),
						fieldWithPath("error.data").type(JsonFieldType.NULL).description("null")
					)
				)
			);
	}

	@Test
	@DisplayName("로그인 API 실패 - 요청 값 검증(이메일) 실패")
	void signinFailWhenParamValidationEmail() throws Exception {
		String signupRequest = """
			{
				"email": "test",
				"password": "password123"
			}
			""";

		mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auth/token")
				.content(signupRequest)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is4xxClientError())
			.andDo(MockMvcRestDocumentationWrapper.document("AUTH API", responseFields(
				fieldWithPath("result").type(JsonFieldType.STRING)
					.description("The result of the operation (e.g., ERROR)"),
				fieldWithPath("data").type(JsonFieldType.NULL).description("The Data is Null When Result is Fail"),
				fieldWithPath("error.code").type(JsonFieldType.STRING).description("E400"),
				fieldWithPath("error.message").type(JsonFieldType.STRING).description("validation error has occurred"),
				fieldWithPath("error.data.email").type(JsonFieldType.STRING).description("이메일 형식이 아닙니다."))));
	}

	@Test
	@DisplayName("로그인 API 실패 - 요청 값 검증(비밀번호) 실패")
	void signinFailWhenParamValidationPassword() throws Exception {
		String signupRequest = """
			{
				"email": "test@test.com",
				"password": "verylongpasswordverylongpasswordverylongpassword"
			}
			""";

		mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auth/token")
				.content(signupRequest)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is4xxClientError())
			.andDo(MockMvcRestDocumentationWrapper.document("AUTH API", responseFields(
				fieldWithPath("result").type(JsonFieldType.STRING)
					.description("The result of the operation (e.g., ERROR)"),
				fieldWithPath("data").type(JsonFieldType.NULL).description("The Data is Null When Result is Fail"),
				fieldWithPath("error.code").type(JsonFieldType.STRING).description("E400"),
				fieldWithPath("error.message").type(JsonFieldType.STRING).description("validation error has occurred"),
				fieldWithPath("error.data.password").type(JsonFieldType.STRING)
					.description("비밀번호는 8자 이상, 20자 이하 여야 합니다"))));
	}
}
