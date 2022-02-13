package com.epam.esm.gcs.persistence.repository.impl;

import com.epam.esm.gcs.persistence.model.GiftCertificateModel;
import com.epam.esm.gcs.persistence.model.GiftCertificateModelContext;
import com.epam.esm.gcs.persistence.model.TagModel;
import com.epam.esm.gcs.persistence.repository.GiftCertificateRepository;
import com.epam.esm.gcs.persistence.tableproperty.GiftCertificateColumn;
import com.epam.esm.gcs.persistence.tableproperty.TagColumn;
import com.epam.esm.gcs.persistence.testapplication.TestApplication;
import com.epam.esm.gcs.persistence.testmanager.TestTablesManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.epam.esm.gcs.persistence.tableproperty.GiftCertificateColumn.CREATE_DATE;
import static com.epam.esm.gcs.persistence.tableproperty.GiftCertificateColumn.DESCRIPTION;
import static com.epam.esm.gcs.persistence.tableproperty.GiftCertificateColumn.DURATION;
import static com.epam.esm.gcs.persistence.tableproperty.GiftCertificateColumn.LAST_UPDATE_DATE;
import static com.epam.esm.gcs.persistence.tableproperty.GiftCertificateColumn.PRICE;
import static com.epam.esm.gcs.persistence.tableproperty.GiftCertificateTagColumn.GIFT_CERTIFICATE_ID;
import static com.epam.esm.gcs.persistence.tableproperty.GiftCertificateTagColumn.TAG_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = TestApplication.class)
@Transactional
class PostgresGiftCertificateRepositoryImplTest {

    private final static String GIFT_CERTIFICATE_TABLE_NAME = "gift_certificate";
    private final static String GIFT_CERTIFICATE_TAG_TABLE_NAME = "gift_certificate_tag";
    private final static String TAG_TABLE = "tag";

    private final GiftCertificateRepository giftCertificateRepository;
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert tagJdbcInsert;
    private final SimpleJdbcInsert giftCertificateJdbcInsert;
    private final SimpleJdbcInsert giftCertificateTagJdbcInsert;

    public PostgresGiftCertificateRepositoryImplTest(GiftCertificateRepository giftCertificateRepository,
                                                     JdbcTemplate jdbcTemplate, TestTablesManager testTablesManager)
            throws SQLException {
        this.giftCertificateRepository = giftCertificateRepository;
        this.jdbcTemplate = jdbcTemplate;
        tagJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName(TAG_TABLE)
                .usingGeneratedKeyColumns(TagColumn.ID.getColumnName()).usingColumns(TagColumn.NAME.getColumnName());
        this.giftCertificateJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName(GIFT_CERTIFICATE_TABLE_NAME)
                .usingGeneratedKeyColumns(GiftCertificateColumn.ID.getColumnName())
                .usingColumns(GiftCertificateColumn.NAME.getColumnName(),
                        DESCRIPTION.getColumnName(), PRICE.getColumnName(),
                        DURATION.getColumnName(), CREATE_DATE.getColumnName(), LAST_UPDATE_DATE.getColumnName());
        this.giftCertificateTagJdbcInsert =
                new SimpleJdbcInsert(jdbcTemplate).withTableName(GIFT_CERTIFICATE_TAG_TABLE_NAME)
                        .usingColumns(GIFT_CERTIFICATE_ID.getColumnName(), TAG_ID.getColumnName());
        testTablesManager.createTables();
    }

    @Test
    void create_returnInputFieldsWithGeneratedFiles_shouldMakeEntityAndRelationsInDataBase() {

        String name = "name";
        String description = "description";
        BigDecimal price = BigDecimal.valueOf(5.2).setScale(2, RoundingMode.HALF_UP);
        int duration = 2;

        String tagName1 = "tag1";
        String tagName2 = "tag2";

        long tagId1 = createTagWithName(tagName1);
        long tagId2 = createTagWithName(tagName2);

        List<TagModel> inputTags = List.of(new TagModel(tagId1, tagName1), new TagModel(tagId2, tagName2));
        GiftCertificateModel inputGiftCertificate = GiftCertificateModel.builder()
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .tags(inputTags)
                .build();

        GiftCertificateModel returnedGiftCertificate = giftCertificateRepository.create(inputGiftCertificate);

        assertNotNull(returnedGiftCertificate);
        assertNotNull(returnedGiftCertificate.getId());
        assertTrue(returnedGiftCertificate.getId() > 0);

        assertEquals(inputGiftCertificate.getName(), returnedGiftCertificate.getName());
        assertEquals(inputGiftCertificate.getDescription(), returnedGiftCertificate.getDescription());
        assertEquals(inputGiftCertificate.getPrice(), returnedGiftCertificate.getPrice());
        assertEquals(inputGiftCertificate.getDuration(), returnedGiftCertificate.getDuration());
        assertIterableEquals(inputGiftCertificate.getTags(), returnedGiftCertificate.getTags());

        assertNotNull(returnedGiftCertificate.getCreateDate());
        assertNotNull(returnedGiftCertificate.getLastUpdateDate());
        assertEquals(returnedGiftCertificate.getCreateDate(), returnedGiftCertificate.getLastUpdateDate());
        assertTrue(LocalDateTime.now().isAfter(returnedGiftCertificate.getCreateDate()));

        long giftCertificateCount = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, GIFT_CERTIFICATE_TABLE_NAME,
                String.format("%s = %d AND %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s'" +
                                " AND %s = '%s'",
                        GiftCertificateColumn.ID.getColumnName(), returnedGiftCertificate.getId(),
                        GiftCertificateColumn.NAME.getColumnName(), returnedGiftCertificate.getName(),
                        DESCRIPTION.getColumnName(), returnedGiftCertificate.getDescription(),
                        PRICE.getColumnName(), returnedGiftCertificate.getPrice(),
                        DURATION.getColumnName(), returnedGiftCertificate.getDuration(),
                        CREATE_DATE.getColumnName(), returnedGiftCertificate.getCreateDate(),
                        LAST_UPDATE_DATE.getColumnName(), returnedGiftCertificate.getLastUpdateDate()));
        assertEquals(1, giftCertificateCount);

        for (TagModel tag : returnedGiftCertificate.getTags()) {
            long giftCertificateTagRelationCount = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
                    GIFT_CERTIFICATE_TAG_TABLE_NAME, String.format("%s = %d AND %s = %d",
                            GIFT_CERTIFICATE_ID.getColumnName(), returnedGiftCertificate.getId(),
                            TAG_ID.getColumnName(), tag.getId()));
            assertEquals(1, giftCertificateTagRelationCount);
        }
    }

    @Test
    void findById_returnGiftCertificate_whenItExists() {
        String name = "name";
        String description = "description";
        BigDecimal price = BigDecimal.valueOf(5.2).setScale(2, RoundingMode.HALF_UP);
        int duration = 2;

        String tagName1 = "tag1";
        String tagName2 = "tag2";

        long tagId1 = createTagWithName(tagName1);
        long tagId2 = createTagWithName(tagName2);

        List<TagModel> relatedTags = List.of(new TagModel(tagId1, tagName1), new TagModel(tagId2, tagName2));
        GiftCertificateModel giftCertificateInDataBase = GiftCertificateModel.builder()
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .tags(relatedTags)
                .build();

        LocalDateTime createDate = LocalDateTime.of(2020, 7, 30, 12, 23, 23);
        LocalDateTime lastUpdateDate = LocalDateTime.of(2020, 8, 28, 11, 34);

        giftCertificateInDataBase.setCreateDate(createDate);
        giftCertificateInDataBase.setLastUpdateDate(lastUpdateDate);
        Long giftCertificateId = createGiftCertificateWithDate(giftCertificateInDataBase);
        relatedTags.forEach(tag -> createGiftCertificateTagRelation(giftCertificateId, tag.getId()));

        GiftCertificateModel expectedReturnedGiftCertificate = giftCertificateInDataBase.withId(giftCertificateId);

        Optional<GiftCertificateModel> returnedGiftCertificate = giftCertificateRepository.findById(giftCertificateId);

        assertTrue(returnedGiftCertificate.isPresent());
        assertEquals(expectedReturnedGiftCertificate, returnedGiftCertificate.get());
    }

    @Test
    void findById_returnOptionalEmpty_whenGiftCertificateDoesNotExist() {
        long giftCertificateId = 3L;

        assertEquals(Optional.empty(), giftCertificateRepository.findById(giftCertificateId));
    }

    @Test
    void updateById_shouldUpdateNotNullFieldsInDataBase() {
        String name = "name";
        String description = "description";
        BigDecimal price = BigDecimal.valueOf(5.2).setScale(2, RoundingMode.HALF_UP);
        int duration = 2;

        String tagName1 = "tag1";
        String tagName2 = "tag2";

        long tagId1 = createTagWithName(tagName1);
        long tagId2 = createTagWithName(tagName2);

        List<TagModel> relatedTags = List.of(new TagModel(tagId1, tagName1), new TagModel(tagId2, tagName2));
        GiftCertificateModel giftCertificateInDataBase = GiftCertificateModel.builder()
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .tags(relatedTags)
                .build();

        LocalDateTime createDate = LocalDateTime.of(2020, 7, 30, 12, 23, 23);
        LocalDateTime lastUpdateDate = LocalDateTime.of(2020, 8, 28, 11, 34);

        giftCertificateInDataBase.setCreateDate(createDate);
        giftCertificateInDataBase.setLastUpdateDate(lastUpdateDate);
        Long giftCertificateId = createGiftCertificateWithDate(giftCertificateInDataBase);
        relatedTags.forEach(tag -> createGiftCertificateTagRelation(giftCertificateId, tag.getId()));

        String newName = "newName";
        int newDuration = 3;
        GiftCertificateModel inputGiftCertificate = GiftCertificateModel.builder()
                .name(newName)
                .duration(newDuration)
                .build();

        GiftCertificateModel updatedGiftCertificate = giftCertificateInDataBase.withId(giftCertificateId);
        updatedGiftCertificate.setName(newName);
        updatedGiftCertificate.setDuration(newDuration);

        LocalDateTime dateTimeBeforeUpdate = lastUpdateDate;

        giftCertificateRepository.updateById(giftCertificateId, inputGiftCertificate);

        long giftCertificateCount = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, GIFT_CERTIFICATE_TABLE_NAME,
                String.format("%s = %d AND %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s'" +
                                " AND %s > '%s'",
                        GiftCertificateColumn.ID.getColumnName(), updatedGiftCertificate.getId(),
                        GiftCertificateColumn.NAME.getColumnName(), updatedGiftCertificate.getName(),
                        DESCRIPTION.getColumnName(), updatedGiftCertificate.getDescription(),
                        PRICE.getColumnName(), updatedGiftCertificate.getPrice(),
                        DURATION.getColumnName(), updatedGiftCertificate.getDuration(),
                        CREATE_DATE.getColumnName(), updatedGiftCertificate.getCreateDate(),
                        LAST_UPDATE_DATE.getColumnName(), dateTimeBeforeUpdate));
        assertEquals(1, giftCertificateCount);

        for (TagModel tag : updatedGiftCertificate.getTags()) {
            long giftCertificateTagRelationCount = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
                    GIFT_CERTIFICATE_TAG_TABLE_NAME, String.format("%s = %d AND %s = %d",
                            GIFT_CERTIFICATE_ID.getColumnName(), updatedGiftCertificate.getId(),
                            TAG_ID.getColumnName(), tag.getId()));
            assertEquals(1, giftCertificateTagRelationCount);
        }
    }

    @Test
    void updateById_shouldUpdateTagRelations() {
        String name = "name";
        String description = "description";
        BigDecimal price = BigDecimal.valueOf(5.2).setScale(2, RoundingMode.HALF_UP);
        int duration = 2;

        String tagName1 = "tag1";
        String tagName2 = "tag2";

        long tagId1 = createTagWithName(tagName1);
        long tagId2 = createTagWithName(tagName2);

        List<TagModel> relatedTags = List.of(new TagModel(tagId1, tagName1), new TagModel(tagId2, tagName2));
        GiftCertificateModel giftCertificateInDataBase = GiftCertificateModel.builder()
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .tags(relatedTags)
                .build();

        LocalDateTime createDate = LocalDateTime.of(2020, 7, 30, 12, 23, 23);
        LocalDateTime lastUpdateDate = LocalDateTime.of(2020, 8, 28, 11, 34);

        giftCertificateInDataBase.setCreateDate(createDate);
        giftCertificateInDataBase.setLastUpdateDate(lastUpdateDate);
        Long giftCertificateId = createGiftCertificateWithDate(giftCertificateInDataBase);
        giftCertificateInDataBase.setId(giftCertificateId);
        relatedTags.forEach(tag -> createGiftCertificateTagRelation(giftCertificateId, tag.getId()));

        TagModel remainingTag = relatedTags.get(0);
        TagModel tagToRemoveRelation = relatedTags.get(1);
        String newTagName = "tag3";
        long newTagId = createTagWithName(newTagName);
        List<TagModel> newTags = List.of(remainingTag, new TagModel(newTagId, newTagName));

        GiftCertificateModel inputGiftCertificate = GiftCertificateModel.builder().tags(newTags).build();

        LocalDateTime dateTimeBeforeUpdate = lastUpdateDate;

        giftCertificateRepository.updateById(giftCertificateId, inputGiftCertificate);

        long giftCertificateCount = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, GIFT_CERTIFICATE_TABLE_NAME,
                String.format("%s = %d AND %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s'" +
                                " AND %s > '%s'",
                        GiftCertificateColumn.ID.getColumnName(), giftCertificateInDataBase.getId(),
                        GiftCertificateColumn.NAME.getColumnName(), giftCertificateInDataBase.getName(),
                        DESCRIPTION.getColumnName(), giftCertificateInDataBase.getDescription(),
                        PRICE.getColumnName(), giftCertificateInDataBase.getPrice(),
                        DURATION.getColumnName(), giftCertificateInDataBase.getDuration(),
                        CREATE_DATE.getColumnName(), giftCertificateInDataBase.getCreateDate(),
                        LAST_UPDATE_DATE.getColumnName(), dateTimeBeforeUpdate));
        assertEquals(1, giftCertificateCount);

        for (TagModel tag : newTags) {
            long giftCertificateTagRelationCount = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
                    GIFT_CERTIFICATE_TAG_TABLE_NAME, String.format("%s = %d AND %s = %d",
                            GIFT_CERTIFICATE_ID.getColumnName(), giftCertificateInDataBase.getId(),
                            TAG_ID.getColumnName(), tag.getId()));
            assertEquals(1, giftCertificateTagRelationCount);
        }

        long removedGiftCertificateTagRelationCount = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
                GIFT_CERTIFICATE_TAG_TABLE_NAME, String.format("%s = %d AND %s = %d",
                        GIFT_CERTIFICATE_ID.getColumnName(), giftCertificateInDataBase.getId(),
                        TAG_ID.getColumnName(), tagToRemoveRelation.getId()));
        assertEquals(0, removedGiftCertificateTagRelationCount);
    }

    @Test
    void delete_shouldDeleteEntryInDataBase() {
        String name = "name";
        String description = "description";
        BigDecimal price = BigDecimal.valueOf(5.2).setScale(2, RoundingMode.HALF_UP);
        int duration = 2;

        String tagName1 = "tag1";
        String tagName2 = "tag2";

        long tagId1 = createTagWithName(tagName1);
        long tagId2 = createTagWithName(tagName2);

        List<TagModel> relatedTags = List.of(new TagModel(tagId1, tagName1), new TagModel(tagId2, tagName2));
        GiftCertificateModel giftCertificateInDataBase = GiftCertificateModel.builder()
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .tags(relatedTags)
                .build();

        LocalDateTime createDate = LocalDateTime.of(2020, 7, 30, 12, 23, 23);
        LocalDateTime lastUpdateDate = LocalDateTime.of(2020, 8, 28, 11, 34);

        giftCertificateInDataBase.setCreateDate(createDate);
        giftCertificateInDataBase.setLastUpdateDate(lastUpdateDate);
        Long giftCertificateId = createGiftCertificateWithDate(giftCertificateInDataBase);
        giftCertificateInDataBase.setId(giftCertificateId);
        relatedTags.forEach(tag -> createGiftCertificateTagRelation(giftCertificateId, tag.getId()));

        giftCertificateRepository.delete(giftCertificateId);

        long giftCertificateCount = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, GIFT_CERTIFICATE_TABLE_NAME,
                String.format("%s = %d AND %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s'" +
                                " AND %s = '%s'",
                        GiftCertificateColumn.ID.getColumnName(), giftCertificateInDataBase.getId(),
                        GiftCertificateColumn.NAME.getColumnName(), giftCertificateInDataBase.getName(),
                        DESCRIPTION.getColumnName(), giftCertificateInDataBase.getDescription(),
                        PRICE.getColumnName(), giftCertificateInDataBase.getPrice(),
                        DURATION.getColumnName(), giftCertificateInDataBase.getDuration(),
                        CREATE_DATE.getColumnName(), giftCertificateInDataBase.getCreateDate(),
                        LAST_UPDATE_DATE.getColumnName(), giftCertificateInDataBase.getLastUpdateDate()));
        assertEquals(0, giftCertificateCount);
    }

    @Test
    void existsById_returnTrue_whenExists() {
        String name = "name";
        String description = "description";
        BigDecimal price = BigDecimal.valueOf(5.2).setScale(2, RoundingMode.HALF_UP);
        int duration = 2;

        String tagName1 = "tag1";
        String tagName2 = "tag2";

        long tagId1 = createTagWithName(tagName1);
        long tagId2 = createTagWithName(tagName2);

        List<TagModel> relatedTags = List.of(new TagModel(tagId1, tagName1), new TagModel(tagId2, tagName2));
        GiftCertificateModel giftCertificateInDataBase = GiftCertificateModel.builder()
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .tags(relatedTags)
                .build();

        LocalDateTime createDate = LocalDateTime.of(2020, 7, 30, 12, 23, 23);
        LocalDateTime lastUpdateDate = LocalDateTime.of(2020, 8, 28, 11, 34);

        giftCertificateInDataBase.setCreateDate(createDate);
        giftCertificateInDataBase.setLastUpdateDate(lastUpdateDate);
        Long giftCertificateId = createGiftCertificateWithDate(giftCertificateInDataBase);
        giftCertificateInDataBase.setId(giftCertificateId);
        relatedTags.forEach(tag -> createGiftCertificateTagRelation(giftCertificateId, tag.getId()));

        assertTrue(giftCertificateRepository.existsById(giftCertificateId));
    }

    @Test
    void existsById_returnFalse_whenDoesNotExist() {
        long notExistedId = 123L;
        assertFalse(giftCertificateRepository.existsById(notExistedId));
    }

    @Test
    void findAll_returnAll_whenContextIsEmpty() {
        String name = "name";
        String description = "description";
        BigDecimal price = BigDecimal.valueOf(5.2).setScale(2, RoundingMode.HALF_UP);
        int duration = 2;
        String name2 = "name2";
        String description2 = "description2";
        BigDecimal price2 = BigDecimal.valueOf(1053).setScale(2, RoundingMode.HALF_UP);
        int duration2 = 2123;

        String tagName1 = "tag1";
        String tagName2 = "tag2";
        String tagName3 = "tag3";

        long tagId1 = createTagWithName(tagName1);
        long tagId2 = createTagWithName(tagName2);
        long tagId3 = createTagWithName(tagName3);

        List<TagModel> relatedTags = List.of(new TagModel(tagId1, tagName1), new TagModel(tagId2, tagName2));
        List<TagModel> relatedTags2 = List.of(new TagModel(tagId2, tagName2), new TagModel(tagId3, tagName3));
        GiftCertificateModel giftCertificateInDataBase = GiftCertificateModel.builder()
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .tags(relatedTags)
                .build();
        GiftCertificateModel giftCertificateInDataBase2 = GiftCertificateModel.builder()
                .name(name2)
                .description(description2)
                .price(price2)
                .duration(duration2)
                .tags(relatedTags2)
                .build();

        LocalDateTime createDate = LocalDateTime.of(2020, 7, 30, 12, 23, 23);
        LocalDateTime lastUpdateDate = LocalDateTime.of(2020, 8, 28, 11, 34);

        LocalDateTime createDate2 = LocalDateTime.of(2021, 7, 11, 2, 2, 2);
        LocalDateTime lastUpdateDate2 = LocalDateTime.of(2021, 10, 22, 11, 34);

        giftCertificateInDataBase.setCreateDate(createDate);
        giftCertificateInDataBase.setLastUpdateDate(lastUpdateDate);
        Long giftCertificateId = createGiftCertificateWithDate(giftCertificateInDataBase);
        giftCertificateInDataBase.setId(giftCertificateId);
        relatedTags.forEach(tag -> createGiftCertificateTagRelation(giftCertificateId, tag.getId()));

        giftCertificateInDataBase2.setCreateDate(createDate2);
        giftCertificateInDataBase2.setLastUpdateDate(lastUpdateDate2);
        Long giftCertificateId2 = createGiftCertificateWithDate(giftCertificateInDataBase2);
        giftCertificateInDataBase2.setId(giftCertificateId2);
        relatedTags2.forEach(tag -> createGiftCertificateTagRelation(giftCertificateId2, tag.getId()));

        List<GiftCertificateModel> expectedReturnedGiftCertificates = List.of(giftCertificateInDataBase,
                giftCertificateInDataBase2);

        GiftCertificateModelContext emptyContext = GiftCertificateModelContext.builder().build();

        assertIterableEquals(expectedReturnedGiftCertificates, giftCertificateRepository.findAll(emptyContext));
    }

    @Test
    void findAll_returnOnlySearchedEntries() {
        String name = "name";
        String description = "description";
        BigDecimal price = BigDecimal.valueOf(5.2).setScale(2, RoundingMode.HALF_UP);
        int duration = 2;
        String nameForSearch = "nameForSearch";
        String description2 = "description2";
        BigDecimal price2 = BigDecimal.valueOf(1053).setScale(2, RoundingMode.HALF_UP);
        int duration2 = 2123;

        String tagName1 = "tag1";
        String tagName2 = "tag2";
        String tagName3 = "tag3";

        long tagId1 = createTagWithName(tagName1);
        long tagId2 = createTagWithName(tagName2);
        long tagId3 = createTagWithName(tagName3);

        List<TagModel> relatedTags = List.of(new TagModel(tagId1, tagName1), new TagModel(tagId2, tagName2));
        List<TagModel> relatedTags2 = List.of(new TagModel(tagId2, tagName2), new TagModel(tagId3, tagName3));
        GiftCertificateModel giftCertificateInDataBase = GiftCertificateModel.builder()
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .tags(relatedTags)
                .build();
        GiftCertificateModel giftCertificateInDataBaseForSearch = GiftCertificateModel.builder()
                .name(nameForSearch)
                .description(description2)
                .price(price2)
                .duration(duration2)
                .tags(relatedTags2)
                .build();

        LocalDateTime createDate = LocalDateTime.of(2020, 7, 30, 12, 23, 23);
        LocalDateTime lastUpdateDate = LocalDateTime.of(2020, 8, 28, 11, 34);

        LocalDateTime createDate2 = LocalDateTime.of(2021, 7, 11, 2, 2, 2);
        LocalDateTime lastUpdateDate2 = LocalDateTime.of(2021, 10, 22, 11, 34);

        giftCertificateInDataBase.setCreateDate(createDate);
        giftCertificateInDataBase.setLastUpdateDate(lastUpdateDate);
        Long giftCertificateId = createGiftCertificateWithDate(giftCertificateInDataBase);
        giftCertificateInDataBase.setId(giftCertificateId);
        relatedTags.forEach(tag -> createGiftCertificateTagRelation(giftCertificateId, tag.getId()));

        giftCertificateInDataBaseForSearch.setCreateDate(createDate2);
        giftCertificateInDataBaseForSearch.setLastUpdateDate(lastUpdateDate2);
        Long giftCertificateId2 = createGiftCertificateWithDate(giftCertificateInDataBaseForSearch);
        giftCertificateInDataBaseForSearch.setId(giftCertificateId2);
        relatedTags2.forEach(tag -> createGiftCertificateTagRelation(giftCertificateId2, tag.getId()));

        List<GiftCertificateModel> expectedReturnedGiftCertificates = List.of(giftCertificateInDataBaseForSearch);

        GiftCertificateModelContext searchContext = GiftCertificateModelContext.builder()
                .searchValue(nameForSearch)
                .build();

        assertIterableEquals(expectedReturnedGiftCertificates, giftCertificateRepository.findAll(searchContext));
    }

    private Long createTagWithName(String name) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(TagColumn.NAME.getColumnName(), name);
        return tagJdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    private Long createGiftCertificateWithDate(GiftCertificateModel giftCertificate) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(GiftCertificateColumn.NAME.getColumnName(), giftCertificate.getName());
        parameters.put(DESCRIPTION.getColumnName(), giftCertificate.getDescription());
        parameters.put(PRICE.getColumnName(), giftCertificate.getPrice());
        parameters.put(DURATION.getColumnName(), giftCertificate.getDuration());
        parameters.put(CREATE_DATE.getColumnName(), giftCertificate.getCreateDate());
        parameters.put(LAST_UPDATE_DATE.getColumnName(), giftCertificate.getLastUpdateDate());
        return giftCertificateJdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    private void createGiftCertificateTagRelation(Long giftCertificateId, Long tagId) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(GIFT_CERTIFICATE_ID.getColumnName(), giftCertificateId);
        parameters.put(TAG_ID.getColumnName(), tagId);
        giftCertificateTagJdbcInsert.execute(parameters);
    }
}