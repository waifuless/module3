package com.epam.esm.gcs.persistence.repository.impl;

import com.epam.esm.gcs.persistence.model.ActualityStateModel;
import com.epam.esm.gcs.persistence.model.GiftCertificateModel;
import com.epam.esm.gcs.persistence.model.GiftCertificateModelContext;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
class PostgresGiftCertificateRepositoryImplTest {

    private final static String GIFT_CERTIFICATE_TABLE_NAME = "gift_certificate";
    private final static String GIFT_CERTIFICATE_TAG_TABLE_NAME = "gift_certificate_tag";
    private final static String TAG_TABLE = "tag";

    private final GiftCertificateRepository giftCertificateRepository;
    private final JdbcTemplate jdbcTemplate;
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
        spaTag = new TagModel(1L, "spa");
        relaxTag = new TagModel(2L, "relax");
        gamingTag = new TagModel(3L, "gaming");
        lgbtTag = new TagModel(4L, "LGBT");

        summerChillGiftCertificate = GiftCertificateModel.builder()
                .id(1L)
                .name("Summer super chill")
                .description("good adventure")
                .price(BigDecimal.valueOf(220.2))
                .duration(120)
                .createDate(LocalDateTime.of(2022, 1, 2, 14, 0, 22, 123 * 1000000))
                .lastUpdateDate(LocalDateTime.of(2022, 1, 2, 14, 0, 22, 123 * 1000000))
                .state(ActualityStateModel.ACTUAL)
                .count(12)
                .successor(null)
                .tags(List.of(spaTag, relaxTag, lgbtTag))
                .build();

        shoppingGiftCertificate = GiftCertificateModel.builder()
                .id(2L)
                .name("Shopping")
                .description("buy anything you want for 40br")
                .price(BigDecimal.valueOf(40))
                .duration(90)
                .createDate(LocalDateTime.of(2022, 1, 3, 22, 22, 21, 789 * 1000000))
                .lastUpdateDate(LocalDateTime.of(2022, 2, 6, 14, 0, 22, 123 * 1000000))
                .state(ActualityStateModel.ACTUAL)
                .count(22)
                .successor(null)
                .tags(List.of(relaxTag))
                .build();

        abilityBoxGiftCertificate = GiftCertificateModel.builder()
                .id(3L)
                .name("AbilityBox game design")
                .description("interesting courses")
                .price(BigDecimal.valueOf(999.99))
                .duration(30)
                .createDate(LocalDateTime.of(2022, 2, 3, 22, 22, 21, 999 * 1000000))
                .lastUpdateDate(LocalDateTime.of(2022, 2, 6, 11, 0, 22, 213 * 1000000))
                .state(ActualityStateModel.ACTUAL)
                .count(32)
                .successor(null)
                .tags(List.of(gamingTag, lgbtTag))
                .build();
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
        JdbcTestUtils.deleteFromTables(jdbcTemplate, GIFT_CERTIFICATE_TABLE_NAME);

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
                String.format("%s = %d AND %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s'" +
                                " AND %s > '%s' AND %s = %d AND %s = %d AND %s IS NULL",
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
                String.format("%s = %d AND %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s'" +
                                " AND %s > '%s' AND %s = %d AND %s = %d AND %s IS NULL",
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
                String.format("%s = %d AND %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s'" +
                                " AND %s = '%s' AND %s = %d AND %s = %d AND %s IS NULL",
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
        JdbcTestUtils.deleteFromTables(jdbcTemplate, GIFT_CERTIFICATE_TABLE_NAME);

        long notExistedId = 1L;
        assertFalse(giftCertificateRepository.existsById(notExistedId));
    }

    @Test
    void findAll_returnAll_whenContextIsEmpty() {
        GiftCertificateModelContext emptyContext = GiftCertificateModelContext.builder().build();

        List<GiftCertificateModel> expectedReturnedGiftCertificates = List.of(summerChillGiftCertificate,
                shoppingGiftCertificate, abilityBoxGiftCertificate);

        List<GiftCertificateModel> returnedGiftCertificates = giftCertificateRepository.findAll(emptyContext);
        assertIterableEquals(expectedReturnedGiftCertificates, returnedGiftCertificates);
    }

    @Test
    void findAll_returnAllOrdered_whenOrderByName() {
        GiftCertificateModelContext emptyContext = GiftCertificateModelContext.builder()
                .sortBy(Map.of("name", SortDirection.ASC))
                .build();

        List<GiftCertificateModel> expectedReturnedGiftCertificates = List.of(abilityBoxGiftCertificate,
                shoppingGiftCertificate, summerChillGiftCertificate);

        List<GiftCertificateModel> returnedGiftCertificates = giftCertificateRepository.findAll(emptyContext);
        assertIterableEquals(expectedReturnedGiftCertificates, returnedGiftCertificates);
    }

    @Test
    void findAll_returnOnlySearchedEntries_bySearchValue() {
        GiftCertificateModelContext searchContext = GiftCertificateModelContext.builder()
                .searchValue("super")
                .build();

        assertIterableEquals(List.of(summerChillGiftCertificate), giftCertificateRepository.findAll(searchContext));
    }

    @Test
    void findAll_returnOnlySearchedEntries_byTagName() {
        GiftCertificateModelContext searchContext = GiftCertificateModelContext.builder()
                .tagName(List.of("relax"))
                .build();

        assertIterableEquals(List.of(summerChillGiftCertificate, shoppingGiftCertificate),
                giftCertificateRepository.findAll(searchContext));
    }
}