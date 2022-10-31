package org.apiaddicts.apitools.apigen.archetypecore;

import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.ApigenRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(repositoryBaseClass = ApigenRepositoryImpl.class)
@SpringBootApplication
public class FakeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FakeApplication.class, args);
    }
}
