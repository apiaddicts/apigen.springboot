package org.apiaddicts.apitools.apigen.generatorcore.utils;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class Mapping {
    private String value;
    private String[] parts;

    public Mapping(String value) {
        this.value = value;
        if (value != null) {
            if (!value.isEmpty() && value.charAt(0) != '/') this.value = "/" + value;
            parts = Stream.of(value.split("/")).filter(s -> !s.trim().isEmpty()).toArray(String[]::new);
        }
    }

    public int size() {
        if (parts == null) return 0;
        return parts.length;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean isFirstVariable() {
        return isVariable(0);
    }

    public boolean isLastVariable(int lastElement) {
        return isVariable(lastElement);
    }

    public boolean hasMoreLevels(int maxSize) {
        return parts != null && size() > 2 && size() <= maxSize;
    }

    public boolean isNotByIdMoreLevels(int maxSize) {
        return hasMoreLevels(maxSize) && !isLastVariable(parts.length - 1);
    }

    public boolean isSearch() {
        return parts.length > 0 ? parts[parts.length - 1].equals("search") : false;
    }

    public boolean isById() {
        return size() == 1 && isFirstVariable();
    }

    public boolean isByIdMoreLevels() {
        return hasMoreLevels(4) && isLastVariable(parts.length - 1);
    }

    public String getValue() {
        return value;
    }

    private boolean isVariable(int i) {
        return isVariable(parts[i]);
    }

    private boolean isVariable(String s) {
        return s.charAt(0) == '{' && s.charAt(s.length() - 1) == '}';
    }

    public String toName() {
        if (parts == null) return "";
        return Stream.of(parts)
                .map(s -> isVariable(s) ? variableToName(s) : partToName(s)).collect(Collectors.joining());
    }

    private String variableToName(String variable) {
        return "By" + StringUtils.capitalize(CustomStringUtils.snakeCaseToCamelCase(variable.substring(1, variable.length() - 1)));
    }

    private String partToName(String part) {
        return StringUtils.capitalize(CustomStringUtils.kebabCaseToCamelCase(part));
    }

}
