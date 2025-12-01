package org.apiaddicts.apitools.apigen.archetypecore.autoconfigure;

import com.fasterxml.jackson.databind.Module;
import org.apiaddicts.apitools.apigen.archetypecore.core.JsonNullableMapper;
import org.apiaddicts.apitools.apigen.archetypecore.core.JsonNullableMapperImpl;
import org.apiaddicts.apitools.apigen.archetypecore.core.advice.ApigenControllerAdvice;
import org.apiaddicts.apitools.apigen.archetypecore.core.errors.ApigenErrorManager;
import org.apiaddicts.apitools.apigen.archetypecore.core.errors.DefaultApigenErrorManager;
import org.apiaddicts.apitools.apigen.archetypecore.core.resource.ResourceNamingTranslator;
import org.apiaddicts.apitools.apigen.archetypecore.core.resource.ResourceNamingTranslatorByReflection;
import org.apiaddicts.apitools.apigen.archetypecore.interceptors.response.ApiResponseBodyAdvice;
import org.apiaddicts.apitools.apigen.archetypecore.interceptors.response.ApigenResponseConverterFilter;
import org.apiaddicts.apitools.apigen.archetypecore.interceptors.update.CachingRequestBodyFilter;
import org.apiaddicts.apitools.apigen.archetypecore.interceptors.update.UpdateRequestBodyAdvice;
import org.apiaddicts.apitools.apigen.archetypecore.interceptors.WebConfig;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import com.fasterxml.jackson.databind.ObjectMapper;

@Import({WebConfig.class})
public class ApigenConfiguration {

	@Bean
	@ConditionalOnMissingBean(ResourceNamingTranslator.class)
	public ResourceNamingTranslator resourceNamingTranslator(ApplicationContext context) {
		return new ResourceNamingTranslatorByReflection(context);
	}

	@Bean
	@ConditionalOnMissingBean(ApigenErrorManager.class)
	public ApigenErrorManager apigenErrorManager(ApigenProperties apigenProperties) {
		return new DefaultApigenErrorManager(apigenProperties);
	}

	@Bean
	@ConditionalOnMissingBean(ApigenControllerAdvice.class)
	public ApigenControllerAdvice apigenControllerAdvice(DefaultApigenErrorManager errorManager) {
		return new ApigenControllerAdvice(errorManager);
	}

	@Bean
	@ConditionalOnMissingBean(ApigenProperties.class)
	public ApigenProperties apigenProperties() {
		return new ApigenProperties();
	}

	@Bean
	@ConditionalOnMissingBean(ApiResponseBodyAdvice.class)
	public ApiResponseBodyAdvice apiResponseBodyAdvice(ApigenProperties properties) {
		return new ApiResponseBodyAdvice(properties);
	}

	@Bean
	@ConditionalOnMissingBean(ApigenResponseConverterFilter.class)
	public ApigenResponseConverterFilter apigenResponseConverterFilter(ObjectMapper objectMapper, ApigenProperties properties) {
		return new ApigenResponseConverterFilter(objectMapper, properties);
	}

	@Bean
	@ConditionalOnMissingBean(UpdateRequestBodyAdvice.class)
	public UpdateRequestBodyAdvice updateRequestBodyAdvice(ObjectMapper mapper) {
		return new UpdateRequestBodyAdvice(mapper);
	}

	@Bean
	@ConditionalOnMissingBean(CachingRequestBodyFilter.class)
	public CachingRequestBodyFilter cachingRequestBodyFilter() {
		return new CachingRequestBodyFilter();
	}

	@Bean
	public Module jsonNullableModule() {
		return new JsonNullableModule();
	}

	@Bean
	@ConditionalOnMissingBean(JsonNullableMapper.class)
	public JsonNullableMapper jsonNullableMapper() {
		return new JsonNullableMapperImpl();
	}
}
