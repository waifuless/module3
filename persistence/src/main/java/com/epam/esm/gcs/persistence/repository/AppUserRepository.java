package com.epam.esm.gcs.persistence.repository;

import com.epam.esm.gcs.persistence.model.AppUserModel;

import java.util.List;

public interface AppUserRepository extends ReadRepository<AppUserModel> {

    List<AppUserModel> findUsersWithHighestPriceAmountOfAllOrders();
}
