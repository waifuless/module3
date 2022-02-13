package com.epam.esm.gcs.persistence.repository.impl;

import com.epam.esm.gcs.persistence.model.GiftCertificateModel;
import com.epam.esm.gcs.persistence.model.GiftCertificateModelContext;
import com.epam.esm.gcs.persistence.model.TagModel;
import com.epam.esm.gcs.persistence.repository.GiftCertificateRepository;
import com.epam.esm.gcs.persistence.repository.TagRepository;
import com.epam.esm.gcs.persistence.tableproperty.GiftCertificateColumn;
import com.epam.esm.gcs.persistence.tableproperty.SortDirection;
import com.epam.esm.gcs.persistence.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostgresGiftCertificateRepositoryImpl implements GiftCertificateRepository {

    private final static String FIND_ALL_QUERY = "SELECT gc FROM GiftCertificateModel gc";
    private final static String DELETE_QUERY = "DELETE FROM GiftCertificateModel gc WHERE gc.id=:id";
    private final static String EXISTS_BY_ID_QUERY = "SELECT COUNT(gc)>0 FROM GiftCertificateModel gc WHERE gc.id=:id";

    private final EntityManager entityManager;
    private final TagRepository tagRepository;

    @Override
    @Transactional
    public GiftCertificateModel create(GiftCertificateModel giftCertificate) {
        GiftCertificateModel giftCertificateCopy = new GiftCertificateModel(giftCertificate);
        LocalDateTime creationDateTime = LocalDateTime.now();

        giftCertificateCopy.setCreateDate(creationDateTime);
        giftCertificateCopy.setLastUpdateDate(creationDateTime);

        entityManager.persist(giftCertificateCopy);
        return giftCertificateCopy;
    }

    @Override
    public Optional<GiftCertificateModel> findById(long id) {
        return Optional.ofNullable(entityManager.find(GiftCertificateModel.class, id));
    }

    @Override
    @Transactional
    public void updateById(long id, GiftCertificateModel giftCertificate) {
        GiftCertificateModel foundGiftCertificate = entityManager.find(GiftCertificateModel.class, id);
        setNotNullFields(giftCertificate, foundGiftCertificate);

        foundGiftCertificate.setLastUpdateDate(LocalDateTime.now());
    }

    @Override
    public void delete(long id) {
        entityManager
                .createQuery(DELETE_QUERY)
                .setParameter("id", id)
                .executeUpdate();
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
    public List<GiftCertificateModel> findAll(GiftCertificateModelContext context) {

        //todo: refactor to make method shorter, remove hard coded JPQL
        List<Pair<String, Object>> params = new ArrayList<>();
        String joinClause = "";
        StringBuilder whereClause = new StringBuilder();
        if (context.getTagName() != null) {
            Optional<TagModel> optionalTag = tagRepository.findByName(context.getTagName());
            if (optionalTag.isEmpty()) {
                return new ArrayList<>();
            }
            joinClause = " JOIN gc.tags tag ";
            Long tagId = optionalTag.get().getId();
            whereClause.append(" tag.id=:tagId ");
            params.add(Pair.of("tagId", tagId));
        }
        if (context.getSearchValue() != null) {
            if (whereClause.length() != 0) {
                whereClause.append(" AND ");
            }
            whereClause.append(" (UPPER(gc.name) LIKE UPPER(:searchValueRegex) " +
                    " OR UPPER(gc.description) LIKE UPPER(:searchValueRegex)) ");
            String searchValueRegex = String.format("%%%s%%", context.getSearchValue());
            params.add(Pair.of("searchValueRegex", searchValueRegex));
        }

        String orderClause = prepareOrderClause(context.getSortBy());
        String findAllByContextQuery = prepareFindAllByContextQuery(joinClause, new String(whereClause), orderClause);

        TypedQuery<GiftCertificateModel> query = entityManager.createQuery(findAllByContextQuery,
                GiftCertificateModel.class);

        for (Pair<String, Object> param : params) {
            query.setParameter(param.getFirst(), param.getSecond());
        }

        return query.getResultList();
    }

    private void setNotNullFields(GiftCertificateModel source, GiftCertificateModel destination) {
        if (source.getName() != null) {
            destination.setName(source.getName());
        }
        if (source.getDescription() != null) {
            destination.setDescription(source.getDescription());
        }
        if (source.getPrice() != null) {
            destination.setPrice(source.getPrice());
        }
        if (source.getDuration() != null) {
            destination.setDuration(source.getDuration());
        }
        if (source.getTags() != null) {
            destination.setTags(source.getTags());
        }
    }

    private String prepareFindAllByContextQuery(String joinClause, String whereClause, String orderClause) {
        StringBuilder findAllByContextQuery = new StringBuilder(FIND_ALL_QUERY);
        if (!joinClause.isBlank()) {
            findAllByContextQuery.append(" ").append(joinClause).append(" ");
        }
        if (!whereClause.isBlank()) {
            findAllByContextQuery.append(" WHERE ").append(whereClause);
        }
        if (!orderClause.isBlank()) {
            findAllByContextQuery.append(" ORDER BY ").append(orderClause);
        }
        return new String(findAllByContextQuery);
    }

    private String prepareOrderClause(Map<GiftCertificateColumn, SortDirection> orderParams) {
        StringBuilder orderClause = new StringBuilder();
        if (orderParams != null && !orderParams.isEmpty()) {
            List<Map.Entry<GiftCertificateColumn, SortDirection>> orderParamEntries =
                    new ArrayList<>(orderParams.entrySet());
            orderClause.append(String.format(" gc.%s %s ", orderParamEntries.get(0).getKey().getColumnName(),
                    orderParamEntries.get(0).getValue().name()));
            for (int i = 1; i < orderParamEntries.size(); i++) {
                orderClause.append(String.format(", gc.%s %s ", orderParamEntries.get(i).getKey().getColumnName(),
                        orderParamEntries.get(i).getValue().name()));
            }
        }
        return new String(orderClause);
    }

    @AllArgsConstructor
    @Getter
    @Setter
    private static class GeneratedQueryWithParams {

        private String query;
        private Object[] params;
    }
}
