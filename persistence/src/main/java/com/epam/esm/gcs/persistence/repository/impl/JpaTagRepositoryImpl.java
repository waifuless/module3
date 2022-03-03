package com.epam.esm.gcs.persistence.repository.impl;

import com.epam.esm.gcs.persistence.model.AppUserModel;
import com.epam.esm.gcs.persistence.model.TagModel;
import com.epam.esm.gcs.persistence.model.UserWithMostlyUsedTagsModel;
import com.epam.esm.gcs.persistence.repository.TagRepository;
import com.epam.esm.gcs.persistence.util.Paginator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaTagRepositoryImpl extends AbstractReadRepository<TagModel> implements TagRepository {

    private static final String DELETE_QUERY = "DELETE FROM TagModel t WHERE t.id=:id";
    private static final String EXISTS_BY_NAME_QUERY = "SELECT COUNT(t)>0 FROM TagModel t WHERE t.name=:name";
    private static final String FIND_BY_NAME_QUERY = "SELECT t FROM TagModel t WHERE t.name=:name";

    private static final String FIND_MOST_WIDELY_USED_TAG_OF_AND_USERS_WITH_HIGHEST_ORDER_PRICE_AMOUNT =
            "SELECT found_user_id    as user_id,\n" +
                    "       found_user_email as user_email,\n" +
                    "       tag.id           as tag_id,\n" +
                    "       tag.name         as tag_name\n" +
                    "FROM tag\n" +
                    "         JOIN gift_certificate_tag gct on tag.id = gct.tag_id\n" +
                    "         JOIN user_order_position uop on gct.gift_certificate_id = uop.gift_certificate_id\n" +
                    "         JOIN user_order uo on uop.user_order_id = uo.id\n" +
                    "         JOIN (SELECT app_user.id as found_user_id, app_user.email as found_user_email\n" +
                    "               FROM app_user\n" +
                    "                        JOIN user_order uo on app_user.id = uo.user_id\n" +
                    "               GROUP BY app_user.id, app_user.email\n" +
                    "               HAVING SUM(uo.price) =\n" +
                    "                      (SELECT MAX(price_sum)\n" +
                    "                       FROM (SELECT SUM(user_order.price) as price_sum\n" +
                    "                             FROM app_user\n" +
                    "                                      JOIN user_order on app_user.id = user_order.user_id\n" +
                    "                             GROUP BY app_user.id) as price_sum_table))\n" +
                    "    as found_users on uo.user_id = found_user_id\n" +
                    "GROUP BY found_user_id, found_user_email, tag.id, tag.name\n" +
                    "HAVING COUNT(uop.user_order_id) =\n" +
                    "       (SELECT MAX(usage)\n" +
                    "        FROM (SELECT COUNT(user_order_position.user_order_id) usage\n" +
                    "              FROM tag\n" +
                    "                       JOIN gift_certificate_tag on tag.id = gift_certificate_tag.tag_id\n" +
                    "                       JOIN user_order_position on gift_certificate_tag.gift_certificate_id =\n" +
                    "                                                   user_order_position.gift_certificate_id\n" +
                    "                       JOIN user_order on user_order_position.user_order_id = user_order.id\n" +
                    "                       JOIN app_user on user_order.user_id = app_user.id\n" +
                    "              WHERE app_user.id = found_user_id\n" +
                    "              GROUP BY tag.id, app_user.id) as usage_table)";

    @PersistenceContext
    private final EntityManager entityManager;

    private final JdbcTemplate jdbcTemplate;

    public JpaTagRepositoryImpl(EntityManager entityManager, Paginator paginator, JdbcTemplate jdbcTemplate) {
        super(entityManager, TagModel.class, paginator);

        this.entityManager = entityManager;
        this.jdbcTemplate = jdbcTemplate;
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
    public List<UserWithMostlyUsedTagsModel> findMostWidelyUsedTagsOfUsersWithHighestOrderPriceAmount() {
        SqlRowSet sqlRowSet = jdbcTemplate
                .queryForRowSet(FIND_MOST_WIDELY_USED_TAG_OF_AND_USERS_WITH_HIGHEST_ORDER_PRICE_AMOUNT);
        List<UserWithMostlyUsedTagsModel> result = new ArrayList<>();
        if (sqlRowSet.next()) {
            AppUserModel currentUser = extractUserWithHighestOrderPriceAmount(sqlRowSet);
            List<TagModel> currentUserTags = new ArrayList<>();
            TagModel currentTag = extractMostWidelyUsedTag(sqlRowSet);
            currentUserTags.add(currentTag);
            while (sqlRowSet.next()) {
                if (sqlRowSet.getLong("user_id") != currentUser.getId()) {
                    result.add(new UserWithMostlyUsedTagsModel(currentUser, currentUserTags));
                    currentUser = extractUserWithHighestOrderPriceAmount(sqlRowSet);
                    currentUserTags.clear();
                }
                currentTag = extractMostWidelyUsedTag(sqlRowSet);
                currentUserTags.add(currentTag);
            }
            result.add(new UserWithMostlyUsedTagsModel(currentUser, currentUserTags));
        }
        return result;
    }

    private AppUserModel extractUserWithHighestOrderPriceAmount(SqlRowSet sqlRowSet) {
        return AppUserModel.builder()
                .id(sqlRowSet.getLong("user_id"))
                .email(sqlRowSet.getString("user_email"))
                .build();
    }

    private TagModel extractMostWidelyUsedTag(SqlRowSet sqlRowSet) {
        return new TagModel(sqlRowSet.getLong("tag_id"), sqlRowSet.getString("tag_name"));
    }
}
