package com.raon.devlog.controller;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.raon.devlog.container.TestContainer;

@Sql(scripts = "/setup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(TestContainer.class)
public class ArticleCategoryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@BeforeEach
	void setup() {
		String deleteAll = "DELETE FROM Category";
		String insert = """
			INSERT INTO Category(name)
			VALUES ('Java'),
			       ('Database'),
			       ('ORM'),
			       ('Network'),
			       ('Docker'),
			       ('Kubernetes'),
			       ('AWS')""";

		jdbcTemplate.execute(deleteAll);
		jdbcTemplate.execute(insert);
	}

	@Test
	@DisplayName("카테고리 리스트 조회 API - 성공")
	void getAllCategoriesSuccess() throws Exception {
		mockMvc.perform(RestDocumentationRequestBuilders.get("/api/category")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is2xxSuccessful())
			.andDo(MockMvcRestDocumentationWrapper.document("Category API", responseFields(
				fieldWithPath("result").type(JsonFieldType.STRING)
					.description("The result of the operation (e.g., SUCCESS)"),
				fieldWithPath("data").type(JsonFieldType.ARRAY).description("Category List"),
				fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("Category Id"),
				fieldWithPath("data[].name").type(JsonFieldType.STRING).description("Category Name"),
				fieldWithPath("error").type(JsonFieldType.NULL).description("Error information (null if no error)"))));

	}
}
