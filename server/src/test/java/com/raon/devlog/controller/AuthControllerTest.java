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
public class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserService userService;

	@Autowired
	private AuthService authService;

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

	@Test
	@DisplayName("토큰 재발급 API 성공")
	void refreshTokenSuccess() throws Exception {
		userService.createUser(new CreateUserInfo("test@test.com", "qwer1234"));
		Token token = authService.generateToken(new SigninRequestInfo("test@test.com", "qwer1234"));

		String refreshTokenRequest = """
				{
					"accessToken": "%s"
				}
			""".formatted(token.accessToken());

		System.out.println(refreshTokenRequest);

		mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auth/token/refresh")
				.content(refreshTokenRequest)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is2xxSuccessful())
			.andDo(MockMvcRestDocumentationWrapper.document("AUTH API", responseFields(
				fieldWithPath("result").type(JsonFieldType.STRING)
					.description("The result of the operation (e.g., SUCCESS)"),
				fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("AccessToken"),
				fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("RefreshToken"),
				fieldWithPath("error").type(JsonFieldType.NULL).description("Error information (null if no error)"))));
	}
}
