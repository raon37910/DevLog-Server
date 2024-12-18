package com.raon.devlog.controller;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class DemoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testDemoEndpoint() throws Exception {
		mockMvc.perform(RestDocumentationRequestBuilders.get("/demo")) // RestDocs 전용 RequestBuilder 사용
			.andExpect(status().isOk()) // HTTP 상태 코드 검증
			.andDo(MockMvcRestDocumentationWrapper
				.document(
					"demo", // 생성될 스니펫 이름
					responseFields(
						fieldWithPath("result")
							.type(JsonFieldType.STRING)
							.description("The result of the operation (e.g., SUCCESS)"),
						fieldWithPath("data")
							.type(JsonFieldType.STRING)
							.description("The data returned by the API (e.g., demo)"),
						fieldWithPath("error")
							.type(JsonFieldType.NULL)
							.description("Error information (null if no error)")
					)
				)
			);
	}
}
