package org.apiaddicts.apitools.apigen.archetypecore.interceptors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.AntPathMatcher;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class ExpandInterceptorTests {

	@Autowired
	private MockMvc mvc;

	@Test
	void givenExplicitAllowedExpand_whenUseOtherExpand_thenError() throws Exception {
		mvc.perform(get("/expand/allowed?$expand=parent,children,other"))
				.andExpect(jsonPath("$.result.errors.length()", is(1)))
				.andExpect(jsonPath("$.result.errors[0].code", is(1103)))
				.andExpect(jsonPath("$.result.errors[0].element", is("other")))
				.andExpect(status().is(400));
	}

    @Test
    void givenExplicitAllowedExpand_whenUseAllowedExpand_thenSuccess() throws Exception {
        mvc.perform(get("/expand/allowed?$expand=parent,children"))
                .andExpect(status().is(200));
    }

    @Test
    void givenExplicitExcludedExpand_whenUseExcludedExpand_thenError() throws Exception {
        mvc.perform(get("/expand/excluded?$expand=invoice,details"))
                .andExpect(jsonPath("$.result.errors.length()", is(1)))
                .andExpect(jsonPath("$.result.errors[0].code", is(1103)))
                .andExpect(jsonPath("$.result.errors[0].element", is("invoice")))
                .andExpect(status().is(400));
    }

    @Test
    void givenExplicitExcludedExpand_whenNotUseExcludedExpand_thenSuccess() throws Exception {
        mvc.perform(get("/expand/excluded?$expand=details,other"))
                .andExpect(status().is(200));
    }

    @Test
    void givenExplicitExpandLevel_whenUseBiggerLevelExpand_thenError() throws Exception {
        mvc.perform(get("/expand/level?$expand=details,other,other.other"))
                .andExpect(jsonPath("$.result.errors.length()", is(1)))
                .andExpect(jsonPath("$.result.errors[0].code", is(1103)))
                .andExpect(jsonPath("$.result.errors[0].element", is("other.other")))
                .andExpect(status().is(400));
    }

    @Test
    void givenExplicitExpandLevel_whenUseAllowedLevelExpand_thenSuccess() throws Exception {
        mvc.perform(get("/expand/level?$expand=details,other"))
                .andExpect(status().is(200));
    }

    @Test
    void givenGenericExpandLevel_whenUseBiggerLevelExpand_thenError() throws Exception {
        mvc.perform(get("/expand/generic?$expand=details,other,other.other,parent.parent.parent"))
                .andExpect(jsonPath("$.result.errors.length()", is(1)))
                .andExpect(jsonPath("$.result.errors[0].code", is(1103)))
                .andExpect(jsonPath("$.result.errors[0].element", is("parent.parent.parent")))
                .andExpect(status().is(400));
    }

    @Test
    void givenGenericExpandLevel_whenUseAllowedLevelExpand_thenSuccess() throws Exception {
        mvc.perform(get("/expand/generic?$expand=details,other, parent.parent"))
                .andExpect(status().is(200));
    }

	@Test
	void givenExplicitExcludedExpand_whenUseExcludedExpandInNotSlashedUrl_thenError() throws Exception {
		mvc.perform(get("/expand/trailing-slash?$expand=slash"))
				.andExpect(status().is(400));
	}

    @Test
    void givenExplicitExcludedExpand_whenUseExcludedExpandInSlashedUrl_thenNotFound() throws Exception {
        mvc.perform(get("/expand/trailing-slash/?$expand=slash"))
                .andExpect(status().is(404));
    }

    @Test
    void givenRexegExplicitIncludedExpand_whenNotUseExcludedExpandInExplicitUrl_thenSuccess() throws Exception {
        mvc.perform(get("/expand/expression/explicit?$expand=other"))
                .andExpect(status().is(200));
    }

    @Test
    void givenRexegExplicitIncludedExpand_whenUseExcludedExpandInExplicitUrl_thenError() throws Exception {
        mvc.perform(get("/expand/expression/explicit?$expand=details"))
                .andExpect(status().is(400));
    }

    @Test
    void givenAnnotationWithAllowed_whenUseAllowedParams_thenSuccess() throws Exception {
        mvc.perform(get("/expand/annotation/allowed?$expand=one,one.two,one.two.three"))
                .andExpect(status().is(200));
    }

    @Test
    void givenAnnotationWithAllowed_whenUseNonAllowedParams_thenError() throws Exception {
        mvc.perform(get("/expand/annotation/allowed?$expand=one,other"))
                .andExpect(jsonPath("$.result.errors.length()", is(1)))
                .andExpect(jsonPath("$.result.errors[0].code", is(1103)))
                .andExpect(jsonPath("$.result.errors[0].element", is("other")))
                .andExpect(status().is(400));
    }

    @Test
    void givenAnnotationWithExcluded_whenUseNonExcludedParams_thenSuccess() throws Exception {
        mvc.perform(get("/expand/annotation/excluded?$expand=other,other.two"))
                .andExpect(status().is(200));
    }

    @Test
    void givenAnnotationWithExcluded_whenUseExcludedParams_thenSuccess() throws Exception {
        mvc.perform(get("/expand/annotation/excluded?$expand=other,other.two,one"))
                .andExpect(jsonPath("$.result.errors.length()", is(1)))
                .andExpect(jsonPath("$.result.errors[0].code", is(1103)))
                .andExpect(jsonPath("$.result.errors[0].element", is("one")))
                .andExpect(status().is(400));
    }

    @Test
    void givenAnnotationWithExcluded_whenUseNonExcludedParamsWithBiggerLevelThanDefault_thenError() throws Exception {
        mvc.perform(get("/expand/annotation/excluded?$expand=other,other.two,other.two.three"))
                .andExpect(jsonPath("$.result.errors.length()", is(1)))
                .andExpect(jsonPath("$.result.errors[0].code", is(1103)))
                .andExpect(jsonPath("$.result.errors[0].element", is("other.two.three")))
                .andExpect(status().is(400));
    }

    @Test
    void givenAnnotationWithExcludedAndLevel_whenUseNonExcludedParamsWithBiggerLevel_thenError() throws Exception {
        mvc.perform(get("/expand/annotation/excluded-and-level?$expand=a,a.b.c,a.b.c.d"))
                .andExpect(jsonPath("$.result.errors.length()", is(1)))
                .andExpect(jsonPath("$.result.errors[0].code", is(1103)))
                .andExpect(jsonPath("$.result.errors[0].element", is("a.b.c.d")))
                .andExpect(status().is(400));
    }

    @Test
    void givenAnnotationWithLevel_whenUseParamsWithBiggerLevel_thenError() throws Exception {
        mvc.perform(get("/expand/annotation/level?$expand=a,a.b.c,a.b.c.d"))
                .andExpect(jsonPath("$.result.errors.length()", is(1)))
                .andExpect(jsonPath("$.result.errors[0].code", is(1103)))
                .andExpect(jsonPath("$.result.errors[0].element", is("a.b.c.d")))
                .andExpect(status().is(400));
    }

    @Test
    void documentedMatchers() {
	    AntPathMatcher m = new AntPathMatcher();
	    assertTrue(m.match("/resources/*", "/resources/id"));

	    assertFalse(m.match("/resources/*", "/resources/id/other"));
	    assertTrue(m.match("/resources/**", "/resources/id/other"));

	    assertTrue(m.match("/resources", "/resources"));
	    assertFalse(m.match("/resources/", "/resources"));

	    assertTrue(m.match("/resources/{identifier}", "/resources/id"));

		assertTrue(m.match("/resources/{path:^(?!other$).*}", "/resources/id"));
		assertFalse(m.match("/resources/{path:^(?!id$).*}", "/resources/id"));

		assertTrue(m.match("/resources/{path:^(?!other$|id$).*}", "/resources/others"));
		assertFalse(m.match("/resources/{path:^(?!other$|id$).*}", "/resources/id"));

		assertTrue(m.match("/resources/{:^(?!other$).*}", "/resources/id"));
		assertFalse(m.match("/resources/{:^(?!id$).*}", "/resources/id"));

		assertTrue(m.match("/resources/{:^(?!other$|id$).*}", "/resources/others"));
		assertFalse(m.match("/resources/{:^(?!other$|id$).*}", "/resources/id"));
    }

}
