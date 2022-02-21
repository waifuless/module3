package com.epam.esm.gcs.persistence.repository.impl;

import com.epam.esm.gcs.persistence.repository.ReadRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public abstract class AbstractReadRepository<T> implements ReadRepository<T> {

    private final EntityManager entityManager;
    private final Class<T> modelClass;

    public AbstractReadRepository(EntityManager entityManager, Class<T> modelClass) {
        this.entityManager = entityManager;
        this.modelClass = modelClass;
    }

    @Override
    public Optional<T> findById(long id) {
        return Optional.ofNullable(entityManager.find(modelClass, id));
    }

    @Override
    public Boolean existsById(long id) {
        return entityManager.find(modelClass, id) != null;
    }

    @Override
    public List<T> findAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(modelClass);

        Root<T> modelRoot = criteriaQuery.from(modelClass);
        criteriaQuery = criteriaQuery.select(modelRoot);

        return entityManager.createQuery(criteriaQuery)
                .getResultList();
    }
}
