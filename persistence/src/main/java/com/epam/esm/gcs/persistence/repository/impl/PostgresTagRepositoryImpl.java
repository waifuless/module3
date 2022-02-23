package com.epam.esm.gcs.persistence.repository.impl;

import com.epam.esm.gcs.persistence.model.AppUserModel;
import com.epam.esm.gcs.persistence.model.TagModel;
import com.epam.esm.gcs.persistence.model.UserWithMostlyUsedTagsModel;
import com.epam.esm.gcs.persistence.repository.TagRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PostgresTagRepositoryImpl extends AbstractReadRepository<TagModel> implements TagRepository {

    private final static String DELETE_QUERY = "DELETE FROM TagModel t WHERE t.id=:id";
    private final static String EXISTS_BY_NAME_QUERY = "SELECT COUNT(t)>0 FROM TagModel t WHERE t.name=:name";
    private final static String FIND_BY_NAME_QUERY = "SELECT t FROM TagModel t WHERE t.name=:name";

    private final static String FIND_USER_MOST_WIDELY_USED_TAGS =
            "SELECT tag FROM AppUserModel au " +
                    " JOIN au.orders u_order" +
                    " JOIN u_order.positions o_position" +
                    " JOIN o_position.giftCertificate.tags tag" +
                    " WHERE au.id=:userId" +
                    " GROUP BY tag" +
                    " HAVING COUNT(o_position) =:maxCount";

    private final static String FIND_USER_TAGS_USAGE_ORDERED_DESC =
            "SELECT COUNT(o_position) FROM AppUserModel au " +
                    " JOIN au.orders u_order" +
                    " JOIN u_order.positions o_position" +
                    " JOIN o_position.giftCertificate.tags tag" +
                    " WHERE au.id=:userId" +
                    " GROUP BY tag" +
                    " ORDER BY COUNT(o_position) DESC";

    private final EntityManager entityManager;

    public PostgresTagRepositoryImpl(EntityManager entityManager) {
        super(entityManager, TagModel.class);

        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public TagModel create(TagModel tagModel) {
        TagModel tagModelCopy = new TagModel(tagModel);

        entityManager.persist(tagModelCopy);
        return tagModelCopy;
    }

    @Override
    @Transactional
    public void delete(long id) {
        entityManager
                .createQuery(DELETE_QUERY)
                .setParameter("id", id)
                .executeUpdate();
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
        try {
            return Optional.of(entityManager.createQuery(FIND_BY_NAME_QUERY,
                            TagModel.class)
                    .setParameter("name", name)
                    .getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<UserWithMostlyUsedTagsModel> findMostWidelyUsedTagsOfUsersById(List<AppUserModel> users) {
        List<UserWithMostlyUsedTagsModel> usersWithMostlyUsedTags = new ArrayList<>();
        for (AppUserModel user : users) {
            try {
                Long maxCount = findMaxUsageCountOfSomeTagByUserId(user.getId());
                List<TagModel> mostlyUsedTags =
                        entityManager.createQuery(FIND_USER_MOST_WIDELY_USED_TAGS, TagModel.class)
                                .setParameter("userId", user.getId())
                                .setParameter("maxCount", maxCount)
                                .getResultList();
                usersWithMostlyUsedTags.add(new UserWithMostlyUsedTagsModel(user, mostlyUsedTags));
            } catch (NoResultException ignored) {
            }
        }
        return usersWithMostlyUsedTags;
    }

    private Long findMaxUsageCountOfSomeTagByUserId(Long userId) {
        return entityManager.createQuery(FIND_USER_TAGS_USAGE_ORDERED_DESC, Long.class)
                .setParameter("userId", userId)
                .setMaxResults(1)
                .getSingleResult();
    }
}
