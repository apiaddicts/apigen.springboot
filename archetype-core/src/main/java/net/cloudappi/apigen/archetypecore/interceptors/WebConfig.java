package net.cloudappi.apigen.archetypecore.interceptors;

import lombok.extern.slf4j.Slf4j;
import net.cloudappi.apigen.archetypecore.autoconfigure.ApigenProperties;
import net.cloudappi.apigen.archetypecore.interceptors.expand.ExpandPathInterceptor;
import net.cloudappi.apigen.archetypecore.interceptors.expand.ExpandAnnotationInterceptor;
import net.cloudappi.apigen.archetypecore.interceptors.trace.TraceIdInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	ApigenProperties properties;

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer.setUseTrailingSlashMatch(false);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		log.debug("Adding interceptors");
		addApigenTraceIdInterceptor(registry);
		addApigenApiInterceptors(registry);

	}

	private void addApigenTraceIdInterceptor(InterceptorRegistry registry) {
		registry.addInterceptor(new TraceIdInterceptor(properties.getTraceHeader()));
	}

	private void addApigenApiInterceptors(InterceptorRegistry registry) {
		ApigenProperties.Api api = properties.getApi();
		List<String> expandPaths = new ArrayList<>();
		api.getPaths().forEach((path, config) -> {
			log.debug("Adding path '{}' interceptors", path);
			ApigenProperties.Api.PathConfig.ExpandConfig expandConfig = config.getExpand();
			if (expandConfig == null) return;
			log.debug("Adding path '{}' expand interceptor {}", path, expandConfig);
			int expandLevel = expandConfig.getLevel() == null ? api.getExpand().getLevel() : expandConfig.getLevel();
			ExpandPathInterceptor interceptor = new ExpandPathInterceptor(expandLevel, expandConfig.getAllowed(), expandConfig.getExcluded());
			registry.addInterceptor(interceptor).addPathPatterns(path);
			expandPaths.add(path);
		});
		log.debug("Adding generic expand interceptor {} excluding paths {}", api.getExpand(), expandPaths);
		ExpandPathInterceptor interceptor = new ExpandPathInterceptor(api.getExpand().getLevel(), Collections.emptySet(), Collections.emptySet());
		registry.addInterceptor(interceptor).excludePathPatterns(expandPaths);
		ExpandAnnotationInterceptor annotatedExpandInterceptor = new ExpandAnnotationInterceptor(api.getExpand().getLevel());
		registry.addInterceptor(annotatedExpandInterceptor);
	}
}
