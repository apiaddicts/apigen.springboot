package org.apiaddicts.apitools.apigen.archetypecore.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@ConditionalOnProperty(prefix = "apigen.documentation", name = "enabled", havingValue = "true")
public class ApigenDocumentationAutoConfiguration {

	@Value("${spring.application.name:}")
	private String name;

	@Value("${spring.application.description:}")
	private String description;

	@Value("${spring.application.version:}")
	private String version;

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
				.build()
				.apiInfo(new ApiInfoBuilder().description(description).title(name).version(version).build())
				.forCodeGeneration(true)
				.useDefaultResponseMessages(false);
	}

	@Bean
	public BeanPostProcessor swaggerBeanPostProcessor() {
		return new SwaggerBeanPostProcessor();
	}

	@Slf4j
	public static class SwaggerBeanPostProcessor implements BeanPostProcessor {

		@Override
		public Object postProcessAfterInitialization(Object bean, String beanName) {
			if (bean instanceof WebMvcProperties) {
				WebMvcProperties properties = (WebMvcProperties) bean;
				String newValue = "/swagger-ui.html";
				log.warn("Overriding value of 'spring.mvc.static-path-pattern' from {} to {}", properties.getStaticPathPattern(), newValue);
				properties.setStaticPathPattern(newValue);
			} else if (bean instanceof ResourceProperties) {
				ResourceProperties properties = (ResourceProperties) bean;
				boolean newValue = true;
				log.warn("Overriding value of 'spring.resources.add-mappings' from {} to {}", properties.isAddMappings(), newValue);
				properties.setAddMappings(newValue);
			}
			return bean;
		}

	}
}
