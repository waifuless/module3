package com.epam.esm.gcs.persistence.repository.impl;

import com.epam.esm.gcs.persistence.model.TagModel;
import com.epam.esm.gcs.persistence.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostgresTagRepositoryImpl implements TagRepository {

    private final static String FIND_ALL_QUERY = "SELECT t FROM TagModel t";
    private final static String DELETE_QUERY = "DELETE FROM TagModel t WHERE t.id=:id";
    private final static String EXISTS_BY_ID_QUERY = "SELECT COUNT(t)>0 FROM TagModel t WHERE t.id=:id";
    private final static String EXISTS_BY_NAME_QUERY = "SELECT COUNT(t)>0 FROM TagModel t WHERE t.name=:name";
    private final static String FIND_BY_NAME_QUERY = "SELECT t FROM TagModel t WHERE t.name=:name";

    private final EntityManager entityManager;

    @Override
    @Transactional
    public TagModel create(TagModel tagModel) {
        //todo: clone model
        entityManager.persist(tagModel);
        return tagModel;
    }

    @Override
    public Optional<TagModel> findById(long id) {
        return Optional.ofNullable(entityManager.find(TagModel.class, id));
    }

    @Override
    public List<TagModel> findAll() {
        return entityManager.createQuery(FIND_ALL_QUERY, TagModel.class)
                .getResultList();
    }

    @Override
    @Transactional
    public void delete(long id) {
        int numberOfDeleted = entityManager
                .createQuery(DELETE_QUERY)
                .setParameter("id", id)
                .executeUpdate();
        if (numberOfDeleted > 1) {
            //todo: think about ex
            throw new RuntimeException();
        }
    }

    @Override
    public Boolean existsById(long id) {
        return entityManager
                .createQuery(EXISTS_BY_ID_QUERY,
                        Boolean.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public Boolean existsByName(String name) {
        return entityManager
                .createQuery(EXISTS_BY_NAME_QUERY,
                        Boolean.class)
                .setParameter("name", name)
                .getSingleResult();
    }

    @Override
    public Optional<TagModel> findByName(String name) {
        return Optional
                .ofNullable(entityManager.createQuery(FIND_BY_NAME_QUERY,
                                TagModel.class)
                        .setParameter("name", name)
                        .getSingleResult());
    }
}
