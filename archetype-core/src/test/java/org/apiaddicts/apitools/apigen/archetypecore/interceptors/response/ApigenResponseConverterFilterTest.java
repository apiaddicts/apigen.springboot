package org.apiaddicts.apitools.apigen.archetypecore.interceptors.response;

import org.apiaddicts.apitools.apigen.archetypecore.autoconfigure.ApigenProperties;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApigenResponseConverterFilterTest {

    private static final JsonMapper MAPPER = JsonMapper.builder().build();

    @Test
    void givenOperations_whenJsonResponse_thenPatchIsApplied() throws Exception {
        ApigenProperties props = new ApigenProperties();
        props.getStandardResponse().setOperations(List.of(
                "{\"op\":\"copy\",\"from\":\"/data\",\"path\":\"/payload\"}",
                "{\"op\":\"move\",\"from\":\"/result/errors\",\"path\":\"/errors\"}",
                "{\"op\":\"move\",\"from\":\"/errors/*/message\",\"path\":\"/errors/*/description\"}",
                "{\"op\":\"remove\",\"path\":\"/data\"}",
                "{\"op\":\"remove\",\"path\":\"/result\"}",
                "{\"op\":\"remove\",\"path\":\"/missing\"}"
        ));
        String body = "{\"data\":{\"id\":1},\"result\":{\"errors\":[{\"message\":\"a\"},{\"message\":\"b\"}]}}";

        MockHttpServletResponse response = new MockHttpServletResponse();
        new ApigenResponseConverterFilter(props)
                .doFilter(new MockHttpServletRequest(), response, new MockFilterChain(jsonServlet(body)));

        JsonNode result = MAPPER.readTree(response.getContentAsString());
        assertEquals(1, result.get("payload").get("id").asInt());
        assertFalse(result.has("data"));
        assertFalse(result.has("result"));
        assertEquals("a", result.get("errors").get(0).get("description").asString());
        assertEquals("b", result.get("errors").get(1).get("description").asString());
        assertFalse(result.get("errors").get(0).has("message"));
    }

    @Test
    void givenNoOperations_whenJsonResponse_thenBodyIsUnchanged() throws Exception {
        String body = "{\"data\":{\"id\":1}}";

        MockHttpServletResponse response = new MockHttpServletResponse();
        new ApigenResponseConverterFilter(new ApigenProperties())
                .doFilter(new MockHttpServletRequest(), response, new MockFilterChain(jsonServlet(body)));

        assertEquals(body, response.getContentAsString());
        assertTrue(response.getContentType().startsWith(MediaType.APPLICATION_JSON_VALUE));
    }

    private static HttpServlet jsonServlet(String body) {
        return new HttpServlet() {
            @Override
            protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
                resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
                resp.getWriter().write(body);
            }
        };
    }
}
