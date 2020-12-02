package net.cloudappi.apigen.archetypecore.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.cloudappi.apigen.archetypecore.core.advice.ApigenControllerAdvice;
import net.cloudappi.apigen.archetypecore.core.errors.ApigenErrorManager;
import net.cloudappi.apigen.archetypecore.core.errors.DefaultApigenErrorManager;
import net.cloudappi.apigen.archetypecore.core.resource.ResourceNamingTranslator;
import net.cloudappi.apigen.archetypecore.core.resource.ResourceNamingTranslatorByReflection;
import net.cloudappi.apigen.archetypecore.interceptors.response.ApiResponseBodyAdvice;
import net.cloudappi.apigen.archetypecore.interceptors.update.CachingRequestBodyFilter;
import net.cloudappi.apigen.archetypecore.interceptors.update.UpdateRequestBodyAdvice;
import net.cloudappi.apigen.archetypecore.interceptors.WebConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import({ApigenDocumentationConfiguration.class, WebConfig.class})
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
	@ConditionalOnMissingBean(UpdateRequestBodyAdvice.class)
	public UpdateRequestBodyAdvice updateRequestBodyAdvice(ObjectMapper mapper) {
		return new UpdateRequestBodyAdvice(mapper);
	}

	@Bean
	@ConditionalOnMissingBean(CachingRequestBodyFilter.class)
	public CachingRequestBodyFilter cachingRequestBodyFilter() {
		return new CachingRequestBodyFilter();
	}
}
