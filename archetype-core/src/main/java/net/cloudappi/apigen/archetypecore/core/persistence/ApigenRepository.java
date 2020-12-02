package net.cloudappi.apigen.archetypecore.core.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.Optional;

@NoRepositoryBean
public interface ApigenRepository<E, K extends Serializable> extends JpaRepository<E, K> {

	ApigenSearchResult<E> search(ApigenSearch search);

	Optional<E> searchById(K id, ApigenSearch search);
}