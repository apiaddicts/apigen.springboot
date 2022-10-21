package org.apiaddicts.apitools.apigen.archetypecore.core.persistence;

import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.executor.ApigenSearchExecutor;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.executor.EntityInfo;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.Optional;

public class ApigenRepositoryImpl<E, K extends Serializable> extends SimpleJpaRepository<E, K> implements ApigenRepository<E, K> {

	private Class<E> clazz;
	private EntityManager em;
	private ApigenSearchExecutor searchExecutor;

	public ApigenRepositoryImpl(JpaEntityInformation<E, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.clazz = entityInformation.getJavaType();
		this.em = entityManager;
		this.searchExecutor = new ApigenSearchExecutor(em, EntityInfo.getInstance(em));
	}

	@Override
	public ApigenSearchResult<E> search(ApigenSearch search) {
		return searchExecutor.search(search.getSelect(), search.getExclude(), search.getOrderBy(), search.getExpand(), search.getFilter(), search.getPagination(), search.getTotal(), clazz);
	}

	@Override
	public Optional<E> searchById(K id, ApigenSearch search) {
		return searchExecutor.searchById(id, search.getSelect(), search.getExclude(), search.getOrderBy(), search.getExpand(), search.getFilter(), clazz);
	}

	@Override
	public long count(ApigenSearch search) {
		return searchExecutor.count(search.getExpand(), search.getFilter(), clazz);
	}
}
