package com.epam.esm.gcs.persistence.repository.impl;

import com.epam.esm.gcs.persistence.model.AppUserModel;
import com.epam.esm.gcs.persistence.repository.AppUserRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class PostgresAppUserRepositoryImpl extends AbstractReadRepository<AppUserModel> implements AppUserRepository {

    public PostgresAppUserRepositoryImpl(EntityManager entityManager) {
        super(entityManager, AppUserModel.class);
    }
}
