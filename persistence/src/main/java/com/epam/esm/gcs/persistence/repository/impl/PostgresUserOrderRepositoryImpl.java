package com.epam.esm.gcs.persistence.repository.impl;

import com.epam.esm.gcs.persistence.model.UserOrderModel;
import com.epam.esm.gcs.persistence.repository.UserOrderRepository;
import com.epam.esm.gcs.persistence.util.Paginator;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class PostgresUserOrderRepositoryImpl extends AbstractReadRepository<UserOrderModel>
        implements UserOrderRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public PostgresUserOrderRepositoryImpl(EntityManager entityManager, Paginator paginator) {
        super(entityManager, UserOrderModel.class, paginator);

        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public UserOrderModel create(UserOrderModel userOrder) {
        UserOrderModel userOrderCopy = new UserOrderModel(userOrder);

        entityManager.persist(userOrderCopy);
        return userOrderCopy;
    }
}
