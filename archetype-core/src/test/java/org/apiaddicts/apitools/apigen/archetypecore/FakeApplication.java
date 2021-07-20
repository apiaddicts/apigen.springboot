package org.apiaddicts.apitools.apigen.archetypecore;

import org.apiaddicts.apitools.apigen.archetypecore.autoconfigure.ApigenApplication;
import org.apiaddicts.apitools.apigen.archetypecore.autoconfigure.ApigenAutoConfiguration;
import org.apiaddicts.apitools.apigen.archetypecore.autoconfigure.ApigenDocumentationAutoConfiguration;
import org.apiaddicts.apitools.apigen.archetypecore.autoconfigure.ApigenWebAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@ApigenApplication
@SpringBootApplication
@Import({ApigenAutoConfiguration.class, ApigenDocumentationAutoConfiguration.class, ApigenWebAutoConfiguration.class})
public class FakeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FakeApplication.class, args);
    }
}
