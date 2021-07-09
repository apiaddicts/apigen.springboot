package org.apiaddicts.apitools.apigen.generatorcore.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomStringUtils {

    private static final String UNDERSCORE = "_";
    private static final String DASH = "-";

    private CustomStringUtils() {
        // Intentional blank
    }

    public static String snakeCaseToCamelCase(String snakeCase) {
        if (snakeCase == null) return null;
        return StringUtils.uncapitalize(
                Stream.of(snakeCase.split(UNDERSCORE))
                        .map(String::toLowerCase).map(StringUtils::capitalize)
                        .collect(Collectors.joining())
        );
    }

    public static String kebabCaseToCamelCase(String snakeCase) {
        if (snakeCase == null) return null;
        return StringUtils.uncapitalize(
                Stream.of(snakeCase.split(DASH))
                        .map(String::toLowerCase).map(StringUtils::capitalize)
                        .collect(Collectors.joining())
        );
    }

    public static String camelCaseToSnakeCase(String camelCase) {
        if (camelCase == null) return null;
        if (camelCase.equals("")) return "";
        StringBuilder result = new StringBuilder();
        char c = camelCase.charAt(0);
        result.append(Character.toLowerCase(c));
        for (int i = 1; i < camelCase.length(); i++) {
            char ch = camelCase.charAt(i);
            if (Character.isUpperCase(ch)) {
                result.append(UNDERSCORE);
                result.append(Character.toLowerCase(ch));
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }
}
