package org.apiaddicts.apitools.apigen.archetypecore.interceptors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
public class ApiResponseBodyAdviceTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void givenApiResponseWithParamsAndTotal_whenInitValue20LimitValue10_thenReturnAllLinks() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                get("/pagination/with-total?name=AAA&$init=20&apes=bbb&$limit=10&$expand=boss")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.metadata.paging.links.self.href", is("/pagination/with-total?name=AAA&$init=20&apes=bbb&$limit=10&$expand=boss")))
                .andExpect(jsonPath("$.metadata.paging.links.previous.href", is("/pagination/with-total?name=AAA&apes=bbb&$limit=10&$expand=boss&$init=10")))
                .andExpect(jsonPath("$.metadata.paging.links.next.href", is("/pagination/with-total?name=AAA&apes=bbb&$limit=10&$expand=boss&$init=30")))
                .andExpect(jsonPath("$.metadata.paging.links.first.href", is("/pagination/with-total?name=AAA&apes=bbb&$limit=10&$expand=boss&$init=0")))
                .andExpect(jsonPath("$.metadata.paging.links.last.href", is("/pagination/with-total?name=AAA&apes=bbb&$limit=10&$expand=boss&$init=90")))
                .andReturn().getResponse();

        // then
        assertEquals(200, response.getStatus());
    }

    @Test
    public void givenApiResponseWithParamsAndWithoutTotal_whenInitValue20LimitValue10_thenReturnAllLinksExceptedLastLink() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                get("/pagination/without-total?name=AAA&$init=20&apes=bbb&$limit=10&$expand=boss")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.metadata.paging.links.self.href", is("/pagination/without-total?name=AAA&$init=20&apes=bbb&$limit=10&$expand=boss")))
                .andExpect(jsonPath("$.metadata.paging.links.previous.href", is("/pagination/without-total?name=AAA&apes=bbb&$limit=10&$expand=boss&$init=10")))
                .andExpect(jsonPath("$.metadata.paging.links.next.href", is("/pagination/without-total?name=AAA&apes=bbb&$limit=10&$expand=boss&$init=30")))
                .andExpect(jsonPath("$.metadata.paging.links.first.href", is("/pagination/without-total?name=AAA&apes=bbb&$limit=10&$expand=boss&$init=0")))
                .andExpect(jsonPath("$.metadata.paging.links.last").doesNotExist())
                .andReturn().getResponse();

        // then
        assertEquals(200, response.getStatus());
    }

    @Test
    public void givenApiResponseWithoutPagination_whenNotPagination_thenNotReturnMetadata() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                get("/pagination/without-pagination?name=AAA&apes=bbb&$expand=boss")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.metadata").doesNotExist())
                .andReturn().getResponse();

        // then
        assertEquals(200, response.getStatus());
    }

    @Test
    public void givenApiResponseWithDefaultPagination_whenPaginationNotDefined_thenReturnOnlySelfNextAndFirstLinks() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                get("/pagination")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.metadata.paging.links.self.href", is("/pagination")))
                .andExpect(jsonPath("$.metadata.paging.links.previous").doesNotExist())
                .andExpect(jsonPath("$.metadata.paging.links.next.href", is("/pagination?$init=25")))
                .andExpect(jsonPath("$.metadata.paging.links.first.href", is("/pagination?$init=0")))
                .andExpect(jsonPath("$.metadata.paging.links.last").doesNotExist())
                .andReturn().getResponse();

        // then
        assertEquals(200, response.getStatus());
    }

    @Test
    public void givenApiResponseWithOutParams_whenNotReturnApiResponse_thenNotDoApiResponseBodyAdvice() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                get("/pagination/no-response?$init=0&$limit=10")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertEquals(200, response.getStatus());
    }

    @Test
    public void givenApiResponseWith200_whenHttpCode200_thenReturnsApiResultWithStatusTrueHttpCode200AndInfoOK() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                get("/apiresult/with-200")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.status", is(true)))
                .andExpect(jsonPath("$.result.http_code", is(200)))
                .andExpect(jsonPath("$.result.info", is("OK")))
                .andExpect(jsonPath("$.result.errors").doesNotExist())
                .andExpect(jsonPath("$.result.trace_id").doesNotExist())
                .andReturn().getResponse();

        // then
        assertEquals(200, response.getStatus());
    }

    @Test
    public void givenApiResponseWith301_whenHttpCode301_thenReturnsApiResultWithStatusTrueHttpCode301AndInfoOK() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                get("/apiresult/with-301")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.status", is(true)))
                .andExpect(jsonPath("$.result.http_code", is(301)))
                .andExpect(jsonPath("$.result.info", is("OK")))
                .andExpect(jsonPath("$.result.errors").doesNotExist())
                .andExpect(jsonPath("$.result.trace_id").doesNotExist())
                .andReturn().getResponse();

        // then
        assertEquals(301, response.getStatus());
    }

    @Test
    public void givenApiResponseWith404_whenHttpCode404_thenReturnsApiResultWithStatusFalseHttpCode404AndInfoERROR() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                get("/apiresult/with-404")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.status", is(false)))
                .andExpect(jsonPath("$.result.http_code", is(404)))
                .andExpect(jsonPath("$.result.info", is("ERROR")))
                .andExpect(jsonPath("$.result.errors").doesNotExist())
                .andExpect(jsonPath("$.result.trace_id").doesNotExist())
                .andReturn().getResponse();

        // then
        assertEquals(404, response.getStatus());
    }

    @Test
    public void givenApiResponseWith503_whenHttpCode503_thenReturnsApiResultWithStatusFalseHttpCode503AndInfoERROR() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                get("/apiresult/with-503")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.status", is(false)))
                .andExpect(jsonPath("$.result.http_code", is(503)))
                .andExpect(jsonPath("$.result.info", is("ERROR")))
                .andExpect(jsonPath("$.result.errors").doesNotExist())
                .andExpect(jsonPath("$.result.trace_id").doesNotExist())
                .andReturn().getResponse();

        // then
        assertEquals(503, response.getStatus());
    }

    @Test
    public void givenApiResponseWithMissingParameter_whenInitDoesExist_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                get("/apiresult/missing-parameter-exception")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.status", is(false)))
                .andExpect(jsonPath("$.result.http_code", is(400)))
                .andExpect(jsonPath("$.result.info", is("ERROR")))
                .andExpect(jsonPath("$.result.errors[0].code", is(1110)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Query parameter '$init' is required")))
                .andExpect(jsonPath("$.result.errors[0].element", is("$init")))
                .andExpect(jsonPath("$.result.trace_id").doesNotExist())
                .andReturn().getResponse();

        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    public void givenApiResponseWithEntityNotFound_whenEntityNotFound_thenReturnErrorWithCodeAndMessage() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                get("/apiresult/entity-notfound-exception-apiresult")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.status", is(false)))
                .andExpect(jsonPath("$.result.http_code", is(404)))
                .andExpect(jsonPath("$.result.info", is("ERROR")))
                .andExpect(jsonPath("$.result.errors[0].code", is(1106)))
                .andExpect(jsonPath("$.result.errors[0].message", is("Element with id 'id' of type 'String' not found")))
                .andExpect(jsonPath("$.result.errors[0].element", is("id")))
                .andExpect(jsonPath("$.result.trace_id").doesNotExist())
                .andReturn().getResponse();

        // then
        assertEquals(404, response.getStatus());
    }

    @Test
    public void givenApiResponseWith200AndTraceId_whenHttpCode200AndTraceIDApiKeyHeader_thenReturnsApiResultWithStatusTrueHttpCode200InfoOKAndTraceIdApikeyvalue() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mvc.perform(
                get("/apiresult/with-200")
                        .accept(MediaType.APPLICATION_JSON).header("api-key-header", "apikeyvalue"))
                .andExpect(jsonPath("$.result.status", is(true)))
                .andExpect(jsonPath("$.result.http_code", is(200)))
                .andExpect(jsonPath("$.result.info", is("OK")))
                .andExpect(jsonPath("$.result.errors").doesNotExist())
                .andExpect(jsonPath("$.result.trace_id", is("apikeyvalue")))
                .andReturn().getResponse();

        // then
        assertEquals(200, response.getStatus());
    }

}
