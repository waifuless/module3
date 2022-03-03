package com.epam.esm.gcs.persistence.repository.impl;

import com.epam.esm.gcs.persistence.model.ActualityStateModel;
import com.epam.esm.gcs.persistence.model.GiftCertificateModel;
import com.epam.esm.gcs.persistence.model.GiftCertificateModelContext;
import com.epam.esm.gcs.persistence.model.PageModel;
import com.epam.esm.gcs.persistence.model.PageParamsModel;
import com.epam.esm.gcs.persistence.model.TagModel;
import com.epam.esm.gcs.persistence.repository.GiftCertificateRepository;
import com.epam.esm.gcs.persistence.tableproperty.SortDirection;
import com.epam.esm.gcs.persistence.testapplication.TestApplication;
import com.epam.esm.gcs.persistence.testtablepropery.GiftCertificateColumn;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.epam.esm.gcs.persistence.testtablepropery.GiftCertificateColumn.COUNT;
import static com.epam.esm.gcs.persistence.testtablepropery.GiftCertificateColumn.CREATE_DATE;
import static com.epam.esm.gcs.persistence.testtablepropery.GiftCertificateColumn.DESCRIPTION;
import static com.epam.esm.gcs.persistence.testtablepropery.GiftCertificateColumn.DURATION;
import static com.epam.esm.gcs.persistence.testtablepropery.GiftCertificateColumn.LAST_UPDATE_DATE;
import static com.epam.esm.gcs.persistence.testtablepropery.GiftCertificateColumn.PRICE;
import static com.epam.esm.gcs.persistence.testtablepropery.GiftCertificateColumn.STATE_ID;
import static com.epam.esm.gcs.persistence.testtablepropery.GiftCertificateColumn.SUCCESSOR_ID;
import static com.epam.esm.gcs.persistence.testtablepropery.GiftCertificateTagColumn.GIFT_CERTIFICATE_ID;
import static com.epam.esm.gcs.persistence.testtablepropery.GiftCertificateTagColumn.TAG_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = "test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@SpringBootTest(classes = TestApplication.class)
@Transactional
class JpaGiftCertificateRepositoryImplTest {

    private static final String GIFT_CERTIFICATE_TABLE_NAME = "gift_certificate";
    private static final String GIFT_CERTIFICATE_TAG_TABLE_NAME = "gift_certificate_tag";
    private static final String TAG_TABLE = "tag";
    private static final String USER_ORDER_TABLE_NAME = "user_order";
    private static final String USER_ORDER_POSITION_TABLE_NAME = "user_order_position";

    private final GiftCertificateRepository giftCertificateRepository;
    private final JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private final EntityManager entityManager;

    private TagModel spaTag;
    private TagModel relaxTag;
    private TagModel gamingTag;
    private TagModel lgbtTag;

    private GiftCertificateModel summerChillGiftCertificate;
    private GiftCertificateModel shoppingGiftCertificate;
    private GiftCertificateModel abilityBoxGiftCertificate;

    @BeforeEach
    private void prepareModels() {
        spaTag = entityManager.find(TagModel.class, 1L);
        relaxTag = entityManager.find(TagModel.class, 2L);
        gamingTag = entityManager.find(TagModel.class, 3L);
        lgbtTag = entityManager.find(TagModel.class, 4L);

        summerChillGiftCertificate = entityManager.find(GiftCertificateModel.class, 1L);
        shoppingGiftCertificate = entityManager.find(GiftCertificateModel.class, 2L);
        abilityBoxGiftCertificate = entityManager.find(GiftCertificateModel.class, 3L);
    }

    @Test
    void create_shouldNotChangeInputModel() {
        String name = "name";
        String description = "description";
        BigDecimal price = BigDecimal.valueOf(5.2).setScale(2, RoundingMode.HALF_UP);
        int duration = 2;

        ActualityStateModel state = ActualityStateModel.ACTUAL;
        Integer count = 123;
        GiftCertificateModel successor = null;

        List<TagModel> inputTags = List.of(spaTag, relaxTag);
        GiftCertificateModel inputGiftCertificate = GiftCertificateModel.builder()
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .state(state)
                .count(count)
                .successor(successor)
                .tags(inputTags)
                .build();
        GiftCertificateModel inputGiftCertificateCopy = new GiftCertificateModel(inputGiftCertificate);

        giftCertificateRepository.create(inputGiftCertificate);
        entityManager.flush();

        assertEquals(inputGiftCertificateCopy, inputGiftCertificate);
    }

    @Test
    void create_returnInputFieldsWithGeneratedFiles_shouldMakeEntityAndRelationsInDataBase() {

        String name = "name";
        String description = "description";
        BigDecimal price = BigDecimal.valueOf(5.2).setScale(2, RoundingMode.HALF_UP);
        int duration = 2;

        ActualityStateModel state = ActualityStateModel.ACTUAL;
        Integer count = 123;

        List<TagModel> inputTags = List.of(spaTag, relaxTag);
        GiftCertificateModel inputGiftCertificate = GiftCertificateModel.builder()
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .state(state)
                .count(count)
                .tags(inputTags)
                .build();

        GiftCertificateModel returnedGiftCertificate = giftCertificateRepository.create(inputGiftCertificate);
        entityManager.flush();

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
                String.format("%s = %d AND %s = '%s' AND %s = '%s'" +
                                " AND %s = '%s'" +
                                " AND %s = '%s'" +
                                " AND date_trunc('second', %s) = date_trunc('second', timestamp '%s')" +
                                " AND date_trunc('second', %s) = date_trunc('second', timestamp '%s')" +
                                " AND %s = %d " +
                                " AND %s = %d AND %s IS NULL",
                        GiftCertificateColumn.ID.getColumnName(), returnedGiftCertificate.getId(),
                        GiftCertificateColumn.NAME.getColumnName(), returnedGiftCertificate.getName(),
                        DESCRIPTION.getColumnName(), returnedGiftCertificate.getDescription(),
                        PRICE.getColumnName(), returnedGiftCertificate.getPrice(),
                        DURATION.getColumnName(), returnedGiftCertificate.getDuration(),
                        CREATE_DATE.getColumnName(), returnedGiftCertificate.getCreateDate(),
                        LAST_UPDATE_DATE.getColumnName(), returnedGiftCertificate.getLastUpdateDate(),
                        STATE_ID.getColumnName(), returnedGiftCertificate.getState().getId(),
                        COUNT.getColumnName(), returnedGiftCertificate.getCount(),
                        SUCCESSOR_ID.getColumnName()));
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
        Optional<GiftCertificateModel> returnedGiftCertificate =
                giftCertificateRepository.findById(abilityBoxGiftCertificate.getId());

        assertTrue(returnedGiftCertificate.isPresent());
        assertEquals(abilityBoxGiftCertificate, returnedGiftCertificate.get());
    }

    @Test
    void findById_returnOptionalEmpty_whenGiftCertificateDoesNotExist() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,
                USER_ORDER_POSITION_TABLE_NAME, USER_ORDER_TABLE_NAME, GIFT_CERTIFICATE_TABLE_NAME);
        entityManager.clear();

        long giftCertificateId = 3L;

        assertEquals(Optional.empty(), giftCertificateRepository.findById(giftCertificateId));
    }

    @Test
    void archiveAndCreateSuccessor_shouldSaveSuccessorUsedNotNullFieldsInDataBase() {

        String newName = summerChillGiftCertificate.getName() + "newSuffix";
        int newDuration = summerChillGiftCertificate.getDuration() + 3;
        GiftCertificateModel inputGiftCertificate = GiftCertificateModel.builder()
                .id(summerChillGiftCertificate.getId())
                .name(newName)
                .duration(newDuration)
                .build();

        summerChillGiftCertificate.setName(newName);
        summerChillGiftCertificate.setDuration(newDuration);

        LocalDateTime dateTimeBeforeUpdate = LocalDateTime.now();

        GiftCertificateModel successor =
                giftCertificateRepository.archiveAndCreateSuccessor(summerChillGiftCertificate.getId(),
                        inputGiftCertificate);
        entityManager.flush();

        long giftCertificateCount = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, GIFT_CERTIFICATE_TABLE_NAME,
                String.format("%s = %d AND %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s'" +
                                " AND date_trunc('second', %s) = date_trunc('second', timestamp '%s') " +
                                " AND date_trunc('second', %s) >= date_trunc('second', timestamp '%s') " +
                                " AND %s = %d AND %s = %d AND %s IS NULL",
                        GiftCertificateColumn.ID.getColumnName(), successor.getId(),
                        GiftCertificateColumn.NAME.getColumnName(), successor.getName(),
                        DESCRIPTION.getColumnName(), successor.getDescription(),
                        PRICE.getColumnName(), successor.getPrice(),
                        DURATION.getColumnName(), successor.getDuration(),
                        CREATE_DATE.getColumnName(), successor.getCreateDate(),
                        LAST_UPDATE_DATE.getColumnName(), dateTimeBeforeUpdate,
                        STATE_ID.getColumnName(), successor.getState().getId(),
                        COUNT.getColumnName(), successor.getCount(),
                        SUCCESSOR_ID.getColumnName()));
        assertEquals(1, giftCertificateCount);

        for (TagModel tag : successor.getTags()) {
            long giftCertificateTagRelationCount = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
                    GIFT_CERTIFICATE_TAG_TABLE_NAME, String.format("%s = %d AND %s = %d",
                            GIFT_CERTIFICATE_ID.getColumnName(), successor.getId(),
                            TAG_ID.getColumnName(), tag.getId()));
            assertEquals(1, giftCertificateTagRelationCount);
        }
    }

    @Test
    void archiveAndCreateSuccessor_shouldUpdateTagRelationsForSuccessor() {

        List<TagModel> relatedTags = summerChillGiftCertificate.getTags();

        TagModel remainingTag = relatedTags.get(0);
        TagModel tagToRemoveRelation = relatedTags.get(1);
        List<TagModel> newTags = List.of(remainingTag, gamingTag);

        GiftCertificateModel inputGiftCertificate = GiftCertificateModel.builder()
                .id(summerChillGiftCertificate.getId())
                .tags(newTags)
                .build();

        LocalDateTime dateTimeBeforeUpdate = LocalDateTime.now();

        GiftCertificateModel successor = giftCertificateRepository
                .archiveAndCreateSuccessor(summerChillGiftCertificate.getId(), inputGiftCertificate);
        entityManager.flush();

        long giftCertificateCount = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, GIFT_CERTIFICATE_TABLE_NAME,
                String.format("%s = %d AND %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s'" +
                                " AND date_trunc('second', %s) = date_trunc('second', timestamp '%s') " +
                                " AND date_trunc('second', %s) >= date_trunc('second', timestamp '%s') " +
                                " AND %s = %d AND %s = %d AND %s IS NULL",
                        GiftCertificateColumn.ID.getColumnName(), successor.getId(),
                        GiftCertificateColumn.NAME.getColumnName(), successor.getName(),
                        DESCRIPTION.getColumnName(), successor.getDescription(),
                        PRICE.getColumnName(), successor.getPrice(),
                        DURATION.getColumnName(), successor.getDuration(),
                        CREATE_DATE.getColumnName(), successor.getCreateDate(),
                        LAST_UPDATE_DATE.getColumnName(), dateTimeBeforeUpdate,
                        STATE_ID.getColumnName(), successor.getState().getId(),
                        COUNT.getColumnName(), successor.getCount(),
                        SUCCESSOR_ID.getColumnName()));
        assertEquals(1, giftCertificateCount);

        for (TagModel tag : newTags) {
            long giftCertificateTagRelationCount = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
                    GIFT_CERTIFICATE_TAG_TABLE_NAME, String.format("%s = %d AND %s = %d",
                            GIFT_CERTIFICATE_ID.getColumnName(), successor.getId(),
                            TAG_ID.getColumnName(), tag.getId()));
            assertEquals(1, giftCertificateTagRelationCount);
        }

        long removedGiftCertificateTagRelationCount = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
                GIFT_CERTIFICATE_TAG_TABLE_NAME, String.format("%s = %d AND %s = %d",
                        GIFT_CERTIFICATE_ID.getColumnName(), successor.getId(),
                        TAG_ID.getColumnName(), tagToRemoveRelation.getId()));
        assertEquals(0, removedGiftCertificateTagRelationCount);
    }

    @Test
    void delete_shouldDeleteEntryInDataBase() {

        giftCertificateRepository.archive(shoppingGiftCertificate.getId());
        entityManager.flush();

        long giftCertificateCount = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, GIFT_CERTIFICATE_TABLE_NAME,
                String.format("%s = %d AND %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s'" +
                                " AND date_trunc('second', %s) = date_trunc('second', timestamp '%s') " +
                                " AND date_trunc('second', %s) = date_trunc('second', timestamp '%s') " +
                                " AND %s = %d AND %s = %d AND %s IS NULL",
                        GiftCertificateColumn.ID.getColumnName(), shoppingGiftCertificate.getId(),
                        GiftCertificateColumn.NAME.getColumnName(), shoppingGiftCertificate.getName(),
                        DESCRIPTION.getColumnName(), shoppingGiftCertificate.getDescription(),
                        PRICE.getColumnName(), shoppingGiftCertificate.getPrice(),
                        DURATION.getColumnName(), shoppingGiftCertificate.getDuration(),
                        CREATE_DATE.getColumnName(), shoppingGiftCertificate.getCreateDate(),
                        LAST_UPDATE_DATE.getColumnName(), shoppingGiftCertificate.getLastUpdateDate(),
                        STATE_ID.getColumnName(), ActualityStateModel.ARCHIVED.getId(),
                        COUNT.getColumnName(), 0,
                        SUCCESSOR_ID.getColumnName()));
        assertEquals(1, giftCertificateCount);
    }

    @Test
    void existsById_returnTrue_whenExists() {
        assertTrue(giftCertificateRepository.existsById(abilityBoxGiftCertificate.getId()));
    }

    @Test
    void existsById_returnFalse_whenDoesNotExist() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,
                USER_ORDER_POSITION_TABLE_NAME, USER_ORDER_TABLE_NAME, GIFT_CERTIFICATE_TABLE_NAME);
        entityManager.clear();

        long notExistedId = 1L;
        assertFalse(giftCertificateRepository.existsById(notExistedId));
    }

    @Test
    void findPage_returnPage_whenContextIsEmpty() {
        GiftCertificateModelContext emptyContext = GiftCertificateModelContext.builder().build();

        List<GiftCertificateModel> expectedContent = List.of(summerChillGiftCertificate,
                shoppingGiftCertificate, abilityBoxGiftCertificate);
        PageParamsModel pageParams = new PageParamsModel(1, 200);
        Long totalCount = 3L;
        PageModel<GiftCertificateModel> expectedPagedModel = new PageModel<>(expectedContent, pageParams, totalCount);

        PageModel<GiftCertificateModel> returnedGiftCertificates = giftCertificateRepository
                .findPage(emptyContext, pageParams);
        assertEquals(expectedPagedModel, returnedGiftCertificates);
    }

    @Test
    void findPage_returnPageOrdered_whenOrderByName() {
        GiftCertificateModelContext emptyContext = GiftCertificateModelContext.builder()
                .sortDirectionByFieldNameMap(Map.of("name", SortDirection.ASC))
                .build();

        List<GiftCertificateModel> expectedContent = List.of(abilityBoxGiftCertificate,
                shoppingGiftCertificate, summerChillGiftCertificate);
        PageParamsModel pageParams = new PageParamsModel(1, 200);
        Long totalCount = 3L;
        PageModel<GiftCertificateModel> expectedPagedModel = new PageModel<>(expectedContent, pageParams, totalCount);

        PageModel<GiftCertificateModel> returnedGiftCertificates = giftCertificateRepository.findPage(emptyContext,
                pageParams);
        assertEquals(expectedPagedModel, returnedGiftCertificates);
    }

    @Test
    void findPage_returnOnlySearchedEntries_bySearchValue() {
        GiftCertificateModelContext searchContext = GiftCertificateModelContext.builder()
                .searchValue("super")
                .build();

        List<GiftCertificateModel> expectedContent = List.of(summerChillGiftCertificate);
        PageParamsModel pageParams = new PageParamsModel(1, 200);
        Long totalCount = 1L;
        PageModel<GiftCertificateModel> expectedPagedModel = new PageModel<>(expectedContent, pageParams, totalCount);

        assertEquals(expectedPagedModel,
                giftCertificateRepository.findPage(searchContext, pageParams));
    }

    @Test
    void findPage_returnOnlySearchedEntries_byTagName() {
        GiftCertificateModelContext searchContext = GiftCertificateModelContext.builder()
                .tagNames(Set.of(relaxTag.getName()))
                .build();
        List<GiftCertificateModel> expectedContent = List.of(summerChillGiftCertificate, shoppingGiftCertificate);
        PageParamsModel pageParams = new PageParamsModel(1, 200);
        Long totalCount = 2L;
        PageModel<GiftCertificateModel> expectedPagedModel = new PageModel<>(expectedContent, pageParams, totalCount);

        assertEquals(expectedPagedModel, giftCertificateRepository.findPage(searchContext, pageParams));
    }

    @Test
    void findPage_returnOnlySearchedEntries_byManyTagName() {
        GiftCertificateModelContext searchContext = GiftCertificateModelContext.builder()
                .tagNames(Set.of(spaTag.getName(), lgbtTag.getName()))
                .build();
        List<GiftCertificateModel> expectedContent = List.of(summerChillGiftCertificate, abilityBoxGiftCertificate);
        PageParamsModel pageParams = new PageParamsModel(1, 200);
        Long totalCount = 2L;
        PageModel<GiftCertificateModel> expectedPagedModel = new PageModel<>(expectedContent, pageParams, totalCount);

        assertEquals(expectedPagedModel,
                giftCertificateRepository.findPage(searchContext, pageParams));
    }
}