package com.epam.esm.gcs.persistence.repository.impl;

import com.epam.esm.gcs.persistence.model.AppUserModel;
import com.epam.esm.gcs.persistence.repository.AppUserRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostgresAppUserRepositoryImpl extends AbstractReadRepository<AppUserModel> implements AppUserRepository {

    private static final String FIND_USERS_WITH_HIGHEST_PRICE_AMOUNT_OF_ALL_ORDERS =
            "SELECT au FROM AppUserModel au JOIN au.orders u_order " +
                    " GROUP BY au" +
                    " HAVING SUM(u_order.price) = :highestPriceAmount";

    private static final String FIND_HIGHEST_PRICE_AMOUNT_OF_ALL_ORDERS =
            "SELECT SUM(u_order.price) FROM AppUserModel au JOIN au.orders u_order" +
                    " GROUP BY au" +
                    " ORDER BY SUM(u_order.price) DESC";

    public PostgresAppUserRepositoryImpl(EntityManager entityManager) {
        super(entityManager, AppUserModel.class);
    }

    @Override
    public List<AppUserModel> findUsersWithHighestPriceAmountOfAllOrders() {
        try {
            BigDecimal highestPriceAmount = entityManager.createQuery(FIND_HIGHEST_PRICE_AMOUNT_OF_ALL_ORDERS,
                            BigDecimal.class)
                    .setMaxResults(1)
                    .getSingleResult();
            return entityManager.createQuery(FIND_USERS_WITH_HIGHEST_PRICE_AMOUNT_OF_ALL_ORDERS, AppUserModel.class)
                    .setParameter("highestPriceAmount", highestPriceAmount)
                    .getResultList();
        } catch (NoResultException ex) {
            return new ArrayList<>();
        }
    }
}
