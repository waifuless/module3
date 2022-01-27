package com.epam.esm.gcs.persistence.repository.impl;

import com.epam.esm.gcs.persistence.mapper.GiftCertificateRowMapper;
import com.epam.esm.gcs.persistence.model.GiftCertificateModel;
import com.epam.esm.gcs.persistence.model.GiftCertificateModelContext;
import com.epam.esm.gcs.persistence.model.TagModel;
import com.epam.esm.gcs.persistence.repository.GiftCertificateRepository;
import com.epam.esm.gcs.persistence.repository.TagRepository;
import com.epam.esm.gcs.persistence.tableproperty.GiftCertificateColumn;
import com.epam.esm.gcs.persistence.tableproperty.SortDirection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.epam.esm.gcs.persistence.tableproperty.GiftCertificateColumn.CREATE_DATE;
import static com.epam.esm.gcs.persistence.tableproperty.GiftCertificateColumn.DESCRIPTION;
import static com.epam.esm.gcs.persistence.tableproperty.GiftCertificateColumn.DURATION;
import static com.epam.esm.gcs.persistence.tableproperty.GiftCertificateColumn.ID;
import static com.epam.esm.gcs.persistence.tableproperty.GiftCertificateColumn.LAST_UPDATE_DATE;
import static com.epam.esm.gcs.persistence.tableproperty.GiftCertificateColumn.NAME;
import static com.epam.esm.gcs.persistence.tableproperty.GiftCertificateColumn.PRICE;
import static com.epam.esm.gcs.persistence.tableproperty.GiftCertificateTagColumn.GIFT_CERTIFICATE_ID;
import static com.epam.esm.gcs.persistence.tableproperty.GiftCertificateTagColumn.TAG_ID;

@Repository
public class PostgresGiftCertificateRepositoryImpl implements GiftCertificateRepository {

    private final static String GIFT_CERTIFICATE_TABLE_NAME = "gift_certificate";
    private final static String GIFT_CERTIFICATE_TAG_TABLE_NAME = "gift_certificate_tag";

    private final static String FIND_ALL_QUERY = "SELECT id as id, name as name, description as description, " +
            " price as price, duration as duration, create_date as create_date, " +
            " last_update_date as last_update_date FROM gift_certificate";
    private final static String FIND_BY_ID_QUERY = "SELECT id as id, name as name, description as description, " +
            " price as price, duration as duration, create_date as create_date, " +
            " last_update_date as last_update_date FROM gift_certificate WHERE id = ?";
    private final static String DELETE_QUERY = "DELETE FROM gift_certificate WHERE id = ?";
    private final static String EXISTS_BY_ID_QUERY = "SELECT (EXISTS(SELECT 1 FROM gift_certificate WHERE id = ?))";
    private final static String REMOVE_ALL_GIFT_CERTIFICATE_TAG_RELATION_QUERY
            = "DELETE FROM gift_certificate_tag WHERE gift_certificate_id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert giftCertificateJdbcInsert;
    private final SimpleJdbcInsert giftCertificateTagJdbcInsert;
    private final GiftCertificateRowMapper giftCertificateRowMapper;
    private final TagRepository tagRepository;

    public PostgresGiftCertificateRepositoryImpl(JdbcTemplate jdbcTemplate,
                                                 GiftCertificateRowMapper giftCertificateRowMapper,
                                                 TagRepository tagRepository) {
        this.giftCertificateRowMapper = giftCertificateRowMapper;
        this.jdbcTemplate = jdbcTemplate;
        this.tagRepository = tagRepository;
        this.giftCertificateJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName(GIFT_CERTIFICATE_TABLE_NAME)
                .usingGeneratedKeyColumns(ID.getColumnName())
                .usingColumns(NAME.getColumnName(), DESCRIPTION.getColumnName(), PRICE.getColumnName(),
                        DURATION.getColumnName(), CREATE_DATE.getColumnName(), LAST_UPDATE_DATE.getColumnName());
        this.giftCertificateTagJdbcInsert =
                new SimpleJdbcInsert(jdbcTemplate).withTableName(GIFT_CERTIFICATE_TAG_TABLE_NAME)
                        .usingColumns(GIFT_CERTIFICATE_ID.getColumnName(), TAG_ID.getColumnName());
    }

    @Override
    public GiftCertificateModel create(GiftCertificateModel giftCertificate) {
        GiftCertificateModel insertedGiftCertificate = insertGiftCertificate(giftCertificate);

        giftCertificate.getTags()
                .forEach(tag -> insertGiftCertificateTagRelation(insertedGiftCertificate.getId(), tag.getId()));

        return insertedGiftCertificate;
    }

    @Override
    public Optional<GiftCertificateModel> findById(long id) {
        try {
            Optional<GiftCertificateModel> optionalGiftCertificate =
                    Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID_QUERY,
                            new Object[]{id}, new int[]{Types.BIGINT}, giftCertificateRowMapper));
            optionalGiftCertificate.ifPresent(giftCertificate -> {
                List<TagModel> tags = tagRepository.findAllByGiftCertificateId(id);
                giftCertificate.setTags(tags);
            });
            return optionalGiftCertificate;
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public void updateById(long id, GiftCertificateModel giftCertificate) {
        GeneratedQueryWithParams queryWithParams = generateUpdateQueryWithParams(id, giftCertificate);
        jdbcTemplate.update(queryWithParams.getQuery(), queryWithParams.getParams());

        List<TagModel> newTags = giftCertificate.getTags();
        if (newTags != null && !newTags.isEmpty()) {
            removeAllGiftCertificateTagRelation(id);
            newTags.forEach(newTag -> insertGiftCertificateTagRelation(id, newTag.getId()));
        }
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update(DELETE_QUERY, id);
    }

    @Override
    public Boolean existsById(long id) {
        return jdbcTemplate.queryForObject(EXISTS_BY_ID_QUERY,
                new Object[]{id}, new int[]{Types.BIGINT},
                Boolean.class);
    }

    @Override
    public List<GiftCertificateModel> findAll(GiftCertificateModelContext context) {
        //todo: refactor to make method shorter (like an update)
        List<Object> params = new ArrayList<>();
        String joinClause = "";
        StringBuilder whereClause = new StringBuilder();
        if (context.getTagName() != null) {
            Optional<TagModel> optionalTag = tagRepository.findByName(context.getTagName());
            if (optionalTag.isEmpty()) {
                return new ArrayList<>();
            }
            joinClause = " JOIN gift_certificate_tag ON gift_certificate.id = gift_certificate_tag" +
                    ".gift_certificate_id ";
            Long tagId = optionalTag.get().getId();
            whereClause.append(String.format(" %s.%s = ? ", GIFT_CERTIFICATE_TAG_TABLE_NAME, TAG_ID.getColumnName()));
            params.add(tagId);
        }
        if (context.getSearchValue() != null) {
            if (whereClause.length() != 0) {
                whereClause.append(" AND ");
            }
            whereClause.append(String.format(" (%s.%s ILIKE ? OR %s.%s ILIKE ?) ", GIFT_CERTIFICATE_TABLE_NAME,
                    NAME.getColumnName(), GIFT_CERTIFICATE_TABLE_NAME, DESCRIPTION.getColumnName()));
            String searchValueRegex = String.format("%%%s%%", context.getSearchValue());
            params.add(searchValueRegex);
            params.add(searchValueRegex);
        }

        String orderClause = prepareOrderClause(context.getSortBy());
        String findAllByContextQuery = prepareFindAllByContextQuery(joinClause, new String(whereClause), orderClause);

        List<GiftCertificateModel> giftCertificates = jdbcTemplate.query(findAllByContextQuery,
                giftCertificateRowMapper, params.toArray());
        giftCertificates.forEach(giftCertificate -> {
            List<TagModel> tags = tagRepository.findAllByGiftCertificateId(giftCertificate.getId());
            giftCertificate.setTags(tags);
        });
        return giftCertificates;
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
            orderClause.append(String.format(" %s %s ", orderParamEntries.get(0).getKey().getColumnName(),
                    orderParamEntries.get(0).getValue().name()));
            for (int i = 1; i < orderParamEntries.size(); i++) {
                orderClause.append(String.format(", %s %s ", orderParamEntries.get(i).getKey().getColumnName(),
                        orderParamEntries.get(i).getValue().name()));
            }
        }
        return new String(orderClause);
    }

    private GiftCertificateModel insertGiftCertificate(GiftCertificateModel giftCertificate) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        Map<String, Object> parameters = new HashMap<>();

        parameters.put(NAME.getColumnName(), giftCertificate.getName());
        parameters.put(DESCRIPTION.getColumnName(), giftCertificate.getDescription());
        parameters.put(PRICE.getColumnName(), giftCertificate.getPrice());
        parameters.put(DURATION.getColumnName(), giftCertificate.getDuration());
        parameters.put(CREATE_DATE.getColumnName(), currentDateTime);
        parameters.put(LAST_UPDATE_DATE.getColumnName(), currentDateTime);

        Long generatedId = giftCertificateJdbcInsert.executeAndReturnKey(parameters).longValue();

        GiftCertificateModel giftCertificateCopy = giftCertificate.withId(generatedId);
        giftCertificateCopy.setCreateDate(currentDateTime);
        giftCertificateCopy.setLastUpdateDate(currentDateTime);
        return giftCertificateCopy;
    }

    private void insertGiftCertificateTagRelation(Long giftCertificateId, Long tagId) {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put(GIFT_CERTIFICATE_ID.getColumnName(), giftCertificateId);
        parameters.put(TAG_ID.getColumnName(), tagId);

        giftCertificateTagJdbcInsert.execute(parameters);
    }

    private void removeAllGiftCertificateTagRelation(Long giftCertificateId) {
        jdbcTemplate.update(REMOVE_ALL_GIFT_CERTIFICATE_TAG_RELATION_QUERY, giftCertificateId);
    }

    private GeneratedQueryWithParams generateUpdateQueryWithParams(long id, GiftCertificateModel giftCertificate) {
        List<Object> params = new ArrayList<>();
        StringBuilder queryParamsPart = new StringBuilder();

        queryParamsPart.append(LAST_UPDATE_DATE.getColumnName()).append(" = ? ");
        params.add(LocalDateTime.now());

        fillParamsAndQuery(params, queryParamsPart, giftCertificate.getName(), NAME);
        fillParamsAndQuery(params, queryParamsPart, giftCertificate.getDescription(), DESCRIPTION);
        fillParamsAndQuery(params, queryParamsPart, giftCertificate.getPrice(), PRICE);
        fillParamsAndQuery(params, queryParamsPart, giftCertificate.getDuration(), DURATION);

        String finalQuery = String.format("UPDATE gift_certificate SET %s WHERE id = ?", queryParamsPart);
        params.add(id);
        return new GeneratedQueryWithParams(finalQuery, params.toArray());
    }

    private void fillParamsAndQuery(List<Object> params, StringBuilder queryParamsPart,
                                    Object param, GiftCertificateColumn column) {
        if (param != null) {
            queryParamsPart.append(" , ").append(column.getColumnName()).append(" = ? ");
            params.add(param);
        }
    }

    @AllArgsConstructor
    @Getter
    @Setter
    private static class GeneratedQueryWithParams {

        private String query;
        private Object[] params;
    }
}
