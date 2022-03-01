package com.epam.esm.gcs.persistence.repository.impl;

import com.epam.esm.gcs.persistence.model.AppUserModel;
import com.epam.esm.gcs.persistence.model.PageModel;
import com.epam.esm.gcs.persistence.model.PageParamsModel;
import com.epam.esm.gcs.persistence.model.UserOrderModel;
import com.epam.esm.gcs.persistence.repository.AppUserRepository;
import com.epam.esm.gcs.persistence.util.Paginator;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
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

    private static final String FIND_USER_ORDERS =
            "SELECT uo FROM UserOrderModel uo WHERE uo.user.id=:userId";
    private static final String FIND_USER_ORDERS_COUNT =
            "SELECT COUNT(uo) FROM UserOrderModel uo WHERE uo.user.id=:userId";

    @PersistenceContext
    private final EntityManager entityManager;

    private final Paginator paginator;

    public PostgresAppUserRepositoryImpl(EntityManager entityManager, Paginator paginator) {
        super(entityManager, AppUserModel.class, paginator);

        this.paginator = paginator;
        this.entityManager = entityManager;
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

    @Override
    public PageModel<UserOrderModel> findUserOrders(Long userId, PageParamsModel pageParams) {
        List<UserOrderModel> content = entityManager.createQuery(FIND_USER_ORDERS, UserOrderModel.class)
                .setParameter("userId", userId)
                .setFirstResult(paginator.findStartPosition(pageParams))
                .setMaxResults(pageParams.getSize())
                .getResultList();
        return new PageModel<>(content, pageParams, countUserOrders(userId));
    }

    @Override
    public Long countUserOrders(Long userId) {
        return entityManager.createQuery(FIND_USER_ORDERS_COUNT, Long.class)
                .setParameter("userId", userId)
                .getSingleResult();
    }
}
