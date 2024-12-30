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
import com.raon.devlog.service.user.CreateUserInfo;
import com.raon.devlog.service.user.UserService;

@Sql(scripts = "/setup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(TestContainer.class)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserService userService;

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
				fieldWithPath("data").type(JsonFieldType.NULL).description("The Data is Null When Result is Fail"),
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
