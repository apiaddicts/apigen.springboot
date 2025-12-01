package org.apiaddicts.apitools.apigen.archetypecore.interceptors.update;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.SneakyThrows;
import org.apiaddicts.apitools.apigen.archetypecore.interceptors.ApigenContext;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import jakarta.servlet.http.HttpServletRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
public class UpdateRequestBodyAdvice extends RequestBodyAdviceAdapter {

    private final ObjectMapper objectMapper;
    private final Map<Class, Map<String, String>> mappings = new HashMap<>();

    public UpdateRequestBodyAdvice(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @SneakyThrows
    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (request instanceof ContentCachingRequestWrapper) {
            byte[] content = ((ContentCachingRequestWrapper) request).getContentAsByteArray();
            if (content[0] == '[') return body; // We avoid arrays
            Set<String> jsonFields = objectMapper.readValue(content, HashMap.class).keySet();
            Map<String, String> mapping = getMapping(body.getClass());
            Set<String> fields = jsonFields.stream().map(f -> mapping.getOrDefault(f, f)).collect(Collectors.toSet());
            ApigenContext.setRequestAttribute("updatedFields", fields);
        }
        return body;
    }

    private Map<String, String> getMapping(Class clazz) {
        if (!mappings.containsKey(clazz)) {
            Map<String, String> mapping = new HashMap<>();
            for (Field field : clazz.getDeclaredFields()) {
                String name = field.getName();
                if (field.isAnnotationPresent(JsonProperty.class)) {
                    String jsonName = field.getDeclaredAnnotation(JsonProperty.class).value();
                    mapping.put(jsonName, name);
                } else {
                    mapping.put(name, name);
                }
            }
            mappings.put(clazz, mapping);
        }
        return mappings.get(clazz);
    }
}
