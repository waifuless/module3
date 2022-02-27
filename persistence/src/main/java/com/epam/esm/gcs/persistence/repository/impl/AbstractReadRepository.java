package com.epam.esm.gcs.persistence.repository.impl;

import com.epam.esm.gcs.persistence.model.PageModel;
import com.epam.esm.gcs.persistence.repository.ReadRepository;
import com.epam.esm.gcs.persistence.util.Paginator;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public abstract class AbstractReadRepository<T> implements ReadRepository<T> {

    protected final EntityManager entityManager;
    protected final Class<T> modelClass;
    protected final Paginator paginator;

    public AbstractReadRepository(EntityManager entityManager, Class<T> modelClass, Paginator paginator) {
        this.entityManager = entityManager;
        this.modelClass = modelClass;
        this.paginator = paginator;
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
    public List<T> findPage(PageModel page) {
        return createFindAllQuery()
                .setFirstResult(paginator.findStartPosition(page))
                .setMaxResults(page.getSize())
                .getResultList();
    }

    private TypedQuery<T> createFindAllQuery() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(modelClass);

        Root<T> modelRoot = criteriaQuery.from(modelClass);
        criteriaQuery.select(modelRoot);
        return entityManager.createQuery(criteriaQuery);
    }
}
