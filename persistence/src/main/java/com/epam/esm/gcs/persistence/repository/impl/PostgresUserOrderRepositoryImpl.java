package com.epam.esm.gcs.persistence.repository.impl;

import com.epam.esm.gcs.persistence.model.UserOrderModel;
import com.epam.esm.gcs.persistence.repository.UserOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostgresUserOrderRepositoryImpl implements UserOrderRepository {

    private final static String FIND_ALL_QUERY = "SELECT uo FROM UserOrderModel uo";

    private final EntityManager entityManager;

    @Override
    @Transactional
    public UserOrderModel create(UserOrderModel userOrder) {
        UserOrderModel userOrderCopy = new UserOrderModel(userOrder);

        userOrderCopy.setCreateDate(LocalDateTime.now());

        entityManager.persist(userOrderCopy);
        return userOrderCopy;
    }

    @Override
    public Optional<UserOrderModel> findById(long id) {
        return Optional.ofNullable(entityManager.find(UserOrderModel.class, id));
    }

    @Override
    public void delete(long id) {

    }

    @Override
    public Boolean existsById(long id) {
        return null;
    }

    @Override
    public List<UserOrderModel> findAll() {
        return entityManager.createQuery(FIND_ALL_QUERY, UserOrderModel.class)
                .getResultList();
    }
}
