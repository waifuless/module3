package com.epam.esm.gcs.persistence.repository.impl;

import com.epam.esm.gcs.persistence.model.AppUserModel;
import com.epam.esm.gcs.persistence.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostgresAppUserRepositoryImpl implements AppUserRepository {

    private final static String FIND_ALL_QUERY = "SELECT au FROM AppUserModel au";

    private final EntityManager entityManager;

    @Override
    public Optional<AppUserModel> findById(long id) {
        return Optional.ofNullable(entityManager.find(AppUserModel.class, id));
    }

    @Override
    public List<AppUserModel> findAll() {
        return entityManager.createQuery(FIND_ALL_QUERY, AppUserModel.class)
                .getResultList();
    }
}
