package com.epam.esm.gcs.persistence.repository;

import com.epam.esm.gcs.persistence.model.AppUserModel;
import com.epam.esm.gcs.persistence.model.PageModel;
import com.epam.esm.gcs.persistence.model.PageParamsModel;
import com.epam.esm.gcs.persistence.model.UserOrderModel;

import java.util.List;

/**
 * Interface for interaction with AppUser
 */
public interface AppUserRepository extends ReadRepository<AppUserModel> {

    /**
     * Finds page of orders by specified user
     *
     * @param userId     - id of user to search
     * @param pageParams - page parameters
     * @return page of orders by specified user
     */
    PageModel<UserOrderModel> findUserOrders(Long userId, PageParamsModel pageParams);

    /**
     * Counts user orders
     *
     * @param userId - id of user to find orders
     * @return - count of all user orders
     */
    Long countUserOrders(Long userId);
}
