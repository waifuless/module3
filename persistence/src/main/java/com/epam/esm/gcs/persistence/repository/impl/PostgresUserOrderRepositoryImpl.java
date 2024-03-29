package com.epam.esm.gcs.persistence.repository.impl;

import com.epam.esm.gcs.persistence.model.UserOrderModel;
import com.epam.esm.gcs.persistence.repository.UserOrderRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@Repository
public class PostgresUserOrderRepositoryImpl extends AbstractReadRepository<UserOrderModel>
        implements UserOrderRepository {

    private final EntityManager entityManager;

    public PostgresUserOrderRepositoryImpl(EntityManager entityManager) {
        super(entityManager, UserOrderModel.class);

        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public UserOrderModel create(UserOrderModel userOrder) {
        UserOrderModel userOrderCopy = new UserOrderModel(userOrder);

        userOrderCopy.setCreateDate(LocalDateTime.now());

        entityManager.persist(userOrderCopy);
        return userOrderCopy;
    }
}
