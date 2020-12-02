package net.cloudappi.apigen.generatorcore.generator.base;

import lombok.extern.slf4j.Slf4j;
import net.cloudappi.apigen.generatorcore.config.Configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class PropertiesGenerator {

    private PropertiesGenerator() {
        // Intentional blank
    }

    public static void generate(Path propertiesDir, Configuration configuration) throws IOException {

        String properties = generateProperties();
        String devProperties = generateDevProperties(configuration);
        Files.createDirectories(propertiesDir);
        write("application.properties", properties, propertiesDir);
        write("application-dev.properties", devProperties, propertiesDir);
        write("application-pre.properties", propertiesDir);
        write("application-pro.properties", propertiesDir);
        write("application-test.properties", propertiesDir);
    }

    private static String generateProperties() {
        StringBuilder p = new StringBuilder();
        add("spring.application.name", "@name@", p);
        add("spring.profiles.active", "dev", p);
        add("logging.level.root", "info", p);
        add("spring.jackson.serialization.fail_on_empty_beans", "false", p);
        add("spring.jackson.default-property-inclusion", "NON_NULL", p);
        add("spring.mvc.throw-exception-if-no-handler-found", "true", p);
        add("spring.resources.add-mappings", "false", p);
        add("management.endpoints.enabled-by-default", "false", p);
        add("management.endpoint.health.enabled", "true", p);
        return p.toString();
    }

    private static String generateDevProperties(Configuration configuration) {
        StringBuilder p = new StringBuilder();
        add("logging.level." + configuration.getBasePackage(), "debug", p);
        add("apigen.documentation.enabled", "true", p);
        add("spring.jpa.show-sql", "true", p);
        return p.toString();
    }

    private static void add(String property, String value, StringBuilder sb) {
        sb.append(property);
        sb.append("=");
        sb.append(value);
        sb.append("\n");
    }

    private static void write(String fileName, Path propertiesDir) {
        write(fileName, "", propertiesDir);
    }

    private static void write(String fileName, String content, Path propertiesDir) {
        File file = Paths.get(propertiesDir.toString(), fileName).toFile();
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content.getBytes());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
