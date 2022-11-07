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

    private boolean hasSize(int num) {
        return size() == num;
    }

    public boolean isEmpty() {
        return hasSize(0);
    }

    public boolean isFirstVariable() {
        return isVariable(0);
    }

    public boolean isSearch() {
        return hasSize(1) && parts[0].equals("search");
    }

    public boolean isById() {
        return hasSize(1) && isFirstVariable();
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
