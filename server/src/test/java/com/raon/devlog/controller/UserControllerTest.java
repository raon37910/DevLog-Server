package com.raon.devlog.controller;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import com.raon.devlog.service.user.CreateUserInfo;
import com.raon.devlog.service.user.UserService;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private UserService userService;

	@BeforeAll
	// FIXME 별도 분리 가능함.
	static void setup(@Autowired JdbcTemplate jdbcTemplate) {
		System.out.println("테이블 생성 시작");
		try {
			// 테이블 생성 SQL 실행
			String createTableSql = """
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

			jdbcTemplate.execute(createTableSql);
			System.out.println("테이블 생성 완료");
		} catch (Exception e) {
			System.out.println("테이블 생성 중 문제가 발생");
			e.printStackTrace();
			throw e;
		}
	}

	@AfterEach
		// FIXME 별도 분리 가능함.
	void cleanup() {
		jdbcTemplate.execute("DELETE FROM User");
	}

	@Test
	@DisplayName("회원가입 API - 성공")
	void signupSuccess() throws Exception {
		String signupRequest = """
			{
				"email": "test@test.com",
				"password": "password123"
			}
			""";

		mockMvc.perform(RestDocumentationRequestBuilders.post("/api/users")
				.content(signupRequest)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is2xxSuccessful())
			.andDo(MockMvcRestDocumentationWrapper.document("USER API", responseFields(
				fieldWithPath("result").type(JsonFieldType.STRING)
					.description("The result of the operation (e.g., SUCCESS)"),
				fieldWithPath("data.email").type(JsonFieldType.STRING).description("The email of the user."),
				fieldWithPath("data.name").type(JsonFieldType.STRING)
					.optional()
					.description("The name of the user (null if not provided)."),
				fieldWithPath("data.description").type(JsonFieldType.STRING)
					.optional()
					.description("The description of the user (null if not provided)."),
				fieldWithPath("data.profileImageUrl").type(JsonFieldType.STRING)
					.optional()
					.description("The profile image URL of the user (null if not provided)."),
				fieldWithPath("data.createTime").type(JsonFieldType.STRING)
					.description("The timestamp when the user was created."),
				fieldWithPath("data.updateTime").type(JsonFieldType.STRING)
					.optional()
					.description("The timestamp when the user was last updated (null if not updated)."),
				fieldWithPath("error").type(JsonFieldType.NULL).description("Error information (null if no error)"))));
	}

	@Test
	@DisplayName("회원가입 API 실패 - 요청 값 검증(이메일) 실패")
	void signupFailWhenParamValidationEmail() throws Exception {
		String signupRequest = """
			{
				"email": "test",
				"password": "password123"
			}
			""";

		mockMvc.perform(RestDocumentationRequestBuilders.post("/api/users")
				.content(signupRequest)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is4xxClientError())
			.andDo(MockMvcRestDocumentationWrapper.document("USER API", responseFields(
				fieldWithPath("result").type(JsonFieldType.STRING)
					.description("The result of the operation (e.g., ERROR)"),
				fieldWithPath("data").type(JsonFieldType.NULL).description("The Data is Null When Result is Fail"),
				fieldWithPath("error.code").type(JsonFieldType.STRING).description("E400"),
				fieldWithPath("error.message").type(JsonFieldType.STRING).description("validation error has occurred"),
				fieldWithPath("error.data.email").type(JsonFieldType.STRING).description("이메일 형식이 아닙니다."))));
	}

	@Test
	@DisplayName("회원가입 API 실패 - 요청 값 검증(비밀번호) 실패")
	void signupFailWhenParamValidationPassword() throws Exception {
		String signupRequest = """
			{
				"email": "test@test.com",
				"password": "verylongpasswordverylongpasswordverylongpassword"
			}
			""";

		mockMvc.perform(RestDocumentationRequestBuilders.post("/api/users")
				.content(signupRequest)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is4xxClientError())
			.andDo(MockMvcRestDocumentationWrapper.document("USER API", responseFields(
				fieldWithPath("result").type(JsonFieldType.STRING)
					.description("The result of the operation (e.g., ERROR)"),
				fieldWithPath("data").type(JsonFieldType.NULL).description("The Data is Null When Result is Fail"),
				fieldWithPath("error.code").type(JsonFieldType.STRING).description("E400"),
				fieldWithPath("error.message").type(JsonFieldType.STRING).description("validation error has occurred"),
				fieldWithPath("error.data.password").type(JsonFieldType.STRING)
					.description("비밀번호는 8자 이상, 20자 이하 여야 합니다"))));
	}

	@Test
	@DisplayName("회원가입 API 실패 - 중복된 이메일")
	void signupFailWhenDuplicatedEmail() throws Exception {
		String signupRequest = """
			{
				"email": "test@test.com",
				"password": "password1234"
			}
			""";

		userService.createUser(new CreateUserInfo("test@test.com", "password1234"));

		mockMvc.perform(RestDocumentationRequestBuilders.post("/api/users")
				.content(signupRequest)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is4xxClientError())
			.andDo(MockMvcRestDocumentationWrapper.document("USER API", responseFields(
				fieldWithPath("result").type(JsonFieldType.STRING)
					.description("The result of the operation (e.g., ERROR)"),
				fieldWithPath("data").type(JsonFieldType.NULL).description("The Data is Null When Result is Fail"),
				fieldWithPath("error.code").type(JsonFieldType.STRING).description("E400"),
				fieldWithPath("error.message").type(JsonFieldType.STRING).description("validation error has occurred"),
				fieldWithPath("error.data").type(JsonFieldType.NULL).description("Null"))));
	}
}
