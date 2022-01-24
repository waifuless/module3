package com.epam.esm.gcs.persistence.repository.impl;

import com.epam.esm.gcs.persistence.mapper.GiftCertificateRowMapper;
import com.epam.esm.gcs.persistence.model.GiftCertificateModel;
import com.epam.esm.gcs.persistence.model.GiftCertificateModelContext;
import com.epam.esm.gcs.persistence.model.TagModel;
import com.epam.esm.gcs.persistence.repository.GiftCertificateRepository;
import com.epam.esm.gcs.persistence.repository.TagRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.epam.esm.gcs.persistence.mapper.GiftCertificateColumn.CREATE_DATE;
import static com.epam.esm.gcs.persistence.mapper.GiftCertificateColumn.DESCRIPTION;
import static com.epam.esm.gcs.persistence.mapper.GiftCertificateColumn.DURATION;
import static com.epam.esm.gcs.persistence.mapper.GiftCertificateColumn.ID;
import static com.epam.esm.gcs.persistence.mapper.GiftCertificateColumn.LAST_UPDATE_DATE;
import static com.epam.esm.gcs.persistence.mapper.GiftCertificateColumn.NAME;
import static com.epam.esm.gcs.persistence.mapper.GiftCertificateColumn.PRICE;
import static com.epam.esm.gcs.persistence.mapper.GiftCertificateTagColumn.GIFT_CERTIFICATE_ID;
import static com.epam.esm.gcs.persistence.mapper.GiftCertificateTagColumn.TAG_ID;

@Repository
public class PostgresGiftCertificateRepositoryImpl implements GiftCertificateRepository {

    private final static String GIFT_CERTIFICATE_TABLE_NAME = "gift_certificate";
    private final static String GIFT_CERTIFICATE_TAG_TABLE_NAME = "gift_certificate_tag";

    private final static String FIND_ALL_QUERY = "SELECT id as id, name as name, description as description, " +
            " price as price, duration as duration, create_date as create_date, " +
            "last_update_date as last_update_date FROM gift_certificate";
    private final static String FIND_BY_ID_QUERY = "SELECT id as id, name as name, description as description, " +
            " price as price, duration as duration, create_date as create_date, " +
            "last_update_date as last_update_date FROM gift_certificate WHERE id = ?";
//    private final static String DELETE_QUERY = "DELETE FROM tag WHERE id = ?";
//    private final static String EXISTS_BY_ID_QUERY = "SELECT (EXISTS(SELECT 1 FROM tag WHERE id = ?))";
//    private final static String EXISTS_BY_NAME_QUERY = "SELECT (EXISTS(SELECT 1 FROM tag WHERE name = ?))";
//    private final static String FIND_BY_NAME_QUERY = "SELECT id as id, name as name FROM tag WHERE name = ?";

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
    public List<GiftCertificateModel> findAll() {
        //todo: replace with findByContext
        //todo: USE IT FOR TEST
        List<GiftCertificateModel> giftCertificates = jdbcTemplate.query(FIND_ALL_QUERY, giftCertificateRowMapper);
        giftCertificates.forEach(giftCertificate -> {
            List<TagModel> tags = tagRepository.findAllByGiftCertificateId(giftCertificate.getId());
            giftCertificate.setTags(tags);
        });
        return giftCertificates;
    }

    @Override
    public void updateById(long id, GiftCertificateModel model) {
    }

    @Override
    public void delete(long id) {

    }

    @Override
    public Boolean existsById(long id) {
        return null;
    }

    @Override
    public List<GiftCertificateModel> findAll(GiftCertificateModelContext context) {
        List<GiftCertificateModel> giftCertificates = jdbcTemplate.query(FIND_ALL_QUERY, giftCertificateRowMapper);
        giftCertificates.forEach(giftCertificate -> {
            List<TagModel> tags = tagRepository.findAllByGiftCertificateId(giftCertificate.getId());
            giftCertificate.setTags(tags);
        });
        return giftCertificates;
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
}
