package com.epam.esm.gcs.persistence.repository.impl;

import com.epam.esm.gcs.persistence.model.AppUserModel;
import com.epam.esm.gcs.persistence.repository.AppUserRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

@Repository
public class PostgresAppUserRepositoryImpl extends AbstractReadRepository<AppUserModel> implements AppUserRepository {

    private final static String FIND_USERS_WITH_HIGHEST_COST_OF_ALL_ORDERS =
            "SELECT au FROM AppUserModel au JOIN au.orders u_order " +
                    " GROUP BY au" +
                    " HAVING SUM(u_order.price) = :maxPrice";

    private final static String FIND_HIGHEST_COST_AMOUNT_OF_ALL_ORDERS =
            "SELECT SUM(u_order.price) FROM AppUserModel au JOIN au.orders u_order" +
                    " GROUP BY au" +
                    " ORDER BY SUM(u_order.price) DESC";

    public PostgresAppUserRepositoryImpl(EntityManager entityManager) {
        super(entityManager, AppUserModel.class);
    }

    //todo: exception if no user or orders exists
    //todo: limit one or return list?
    //todo: rename price and cost to smth
    @Override
    public List<AppUserModel> findUsersWithHighestCostOfAllOrders() {
        BigDecimal highestCost = entityManager.createQuery(FIND_HIGHEST_COST_AMOUNT_OF_ALL_ORDERS,
                        BigDecimal.class)
                .setMaxResults(1)
                .getSingleResult();
        return entityManager.createQuery(FIND_USERS_WITH_HIGHEST_COST_OF_ALL_ORDERS, AppUserModel.class)
                .setParameter("maxPrice", highestCost)
                .getResultList();
    }
}
