package com.epam.esm.gcs.persistence.repository.impl;

import com.epam.esm.gcs.persistence.model.AppUserModel;
import com.epam.esm.gcs.persistence.model.PageModel;
import com.epam.esm.gcs.persistence.model.PageParamsModel;
import com.epam.esm.gcs.persistence.model.UserOrderModel;
import com.epam.esm.gcs.persistence.repository.AppUserRepository;
import com.epam.esm.gcs.persistence.util.Paginator;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class JpaAppUserRepositoryImpl extends AbstractReadRepository<AppUserModel> implements AppUserRepository {

    private static final String FIND_USER_ORDERS =
            "SELECT uo FROM UserOrderModel uo WHERE uo.user.id=:userId";
    private static final String FIND_USER_ORDERS_COUNT =
            "SELECT COUNT(uo) FROM UserOrderModel uo WHERE uo.user.id=:userId";

    @PersistenceContext
    private final EntityManager entityManager;

    private final Paginator paginator;

    public JpaAppUserRepositoryImpl(EntityManager entityManager, Paginator paginator) {
        super(entityManager, AppUserModel.class, paginator);

        this.paginator = paginator;
        this.entityManager = entityManager;
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
