package org.apiaddicts.apitools.apigen.archetypecore.interceptors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class TraceIdInterceptorTests {

	@Autowired
	private MockMvc mvc;

	@Test
	void givenTraceIdConfigured_whenApiReceivesCallWithTraceId_thenApigenContextHasTheValue() throws Exception {
		MvcResult result = mvc.perform(get("/trace")
				.header("api-key-header", "test"))
				.andExpect(status().is(200))
				.andReturn();

		String response = result.getResponse().getContentAsString();
		assertEquals("test", response);
	}

}
