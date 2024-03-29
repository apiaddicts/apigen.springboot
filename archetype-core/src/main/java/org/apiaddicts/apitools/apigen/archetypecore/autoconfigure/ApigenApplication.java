package org.apiaddicts.apitools.apigen.archetypecore.autoconfigure;

import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.ApigenRepositoryImpl;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.lang.annotation.*;


@Inherited
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@EnableJpaRepositories(repositoryBaseClass = ApigenRepositoryImpl.class)
@Import(ApigenConfiguration.class)
public @interface ApigenApplication {
}
