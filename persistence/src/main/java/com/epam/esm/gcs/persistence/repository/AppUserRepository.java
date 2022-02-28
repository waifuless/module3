package com.epam.esm.gcs.persistence.repository;

import com.epam.esm.gcs.persistence.model.AppUserModel;
import com.epam.esm.gcs.persistence.model.PageModel;
import com.epam.esm.gcs.persistence.model.PageParamsModel;
import com.epam.esm.gcs.persistence.model.UserOrderModel;

import java.util.List;

public interface AppUserRepository extends ReadRepository<AppUserModel> {

    List<AppUserModel> findUsersWithHighestPriceAmountOfAllOrders();

    PageModel<UserOrderModel> findUserOrders(Long userId, PageParamsModel pageParams);

    Long countUserOrders(Long userId);
}
