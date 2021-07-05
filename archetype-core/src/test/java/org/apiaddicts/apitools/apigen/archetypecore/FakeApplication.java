package org.apiaddicts.apitools.apigen.archetypecore;

import org.apiaddicts.apitools.apigen.archetypecore.autoconfigure.ApigenApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@ApigenApplication
@SpringBootApplication
public class FakeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FakeApplication.class, args);
    }
}
