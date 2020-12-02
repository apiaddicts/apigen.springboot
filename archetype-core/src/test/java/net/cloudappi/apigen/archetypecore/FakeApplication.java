package net.cloudappi.apigen.archetypecore;

import net.cloudappi.apigen.archetypecore.autoconfigure.ApigenApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@ApigenApplication
@SpringBootApplication
public class FakeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FakeApplication.class, args);
    }
}
