package com.epam.esm.gcs.persistence.repository;

import com.epam.esm.gcs.persistence.model.AppUserModel;

import java.util.List;

public interface AppUserRepository extends ReadRepository<AppUserModel> {

    /**
     * Finds all TagModels
     *
     * @return List of all AppUserModel
     */
    List<AppUserModel> findAll();
}
