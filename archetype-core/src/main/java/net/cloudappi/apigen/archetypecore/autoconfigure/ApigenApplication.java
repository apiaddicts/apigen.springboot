package net.cloudappi.apigen.archetypecore.autoconfigure;

import net.cloudappi.apigen.archetypecore.core.persistence.ApigenRepositoryImpl;
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
