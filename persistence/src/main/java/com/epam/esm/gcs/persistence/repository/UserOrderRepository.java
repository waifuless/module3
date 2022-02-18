package com.epam.esm.gcs.persistence.repository;

import com.epam.esm.gcs.persistence.model.UserOrderModel;

import java.util.List;

public interface UserOrderRepository extends CrdRepository<UserOrderModel> {

    /**
     * Finds all TagModels
     *
     * @return List of all AppUserModel
     */
    List<UserOrderModel> findAll();
}
