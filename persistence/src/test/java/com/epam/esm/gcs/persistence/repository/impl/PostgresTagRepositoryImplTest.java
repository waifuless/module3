package com.epam.esm.gcs.persistence.repository.impl;

import com.epam.esm.gcs.persistence.model.AppUserModel;
import com.epam.esm.gcs.persistence.model.TagModel;
import com.epam.esm.gcs.persistence.model.UserWithMostlyUsedTagsModel;
import com.epam.esm.gcs.persistence.repository.TagRepository;
import com.epam.esm.gcs.persistence.testapplication.TestApplication;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.gcs.persistence.testtablepropery.TagColumn.ID;
import static com.epam.esm.gcs.persistence.testtablepropery.TagColumn.NAME;
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
class PostgresTagRepositoryImplTest {

    private static final String TAG_TABLE = "tag";

    private final TagRepository tagRepository;
    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;

    private TagModel spaTag;
    private TagModel relaxTag;
    private TagModel gamingTag;
    private TagModel lgbtTag;

    @BeforeEach
    private void prepareModels() {
        spaTag = entityManager.find(TagModel.class, 1L);
        relaxTag = entityManager.find(TagModel.class, 2L);
        gamingTag = entityManager.find(TagModel.class, 3L);
        lgbtTag = entityManager.find(TagModel.class, 4L);
    }

    @Test
    void create_shouldNotChangeInputModel() {
        String name = "someName123321222";
        TagModel inputTagModel = new TagModel(name);
        TagModel inputTagModelCopy = new TagModel(inputTagModel);

        tagRepository.create(inputTagModel);
        entityManager.flush();

        assertEquals(inputTagModelCopy, inputTagModel);
    }

    @Test
    void create_returnTagWithValidIdAndSameName() {
        TagModel inputModel = new TagModel("someVerySimpleName");
        TagModel returnedModel = tagRepository.create(inputModel);
        assertNotNull(returnedModel);
        assertNotNull(returnedModel.getId());
        assertTrue(returnedModel.getId() > 0);
        assertEquals(inputModel.getName(), returnedModel.getName());
    }

    @Test
    void create_returnedTagModelEqualsModelInDatabase() {
        String name = "lola";
        TagModel returnedModel = tagRepository.create(new TagModel(name));
        entityManager.flush();

        assertNotNull(returnedModel);
        assertNotNull(returnedModel.getId());
        assertEquals(name, returnedModel.getName());

        long count = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, TAG_TABLE,
                String.format("%s = %d AND %s = '%s'", ID.getColumnName(), returnedModel.getId(),
                        NAME.getColumnName(), returnedModel.getName()));
        assertEquals(1, count);
    }

    @Test
    void findById_returnModel_ifExistInDatabase() {
        Optional<TagModel> readModel = tagRepository.findById(spaTag.getId());
        assertTrue(readModel.isPresent());
        assertEquals(spaTag, readModel.get());
    }

    @Test
    void findById_returnOptionalEmpty_ifDoNotExistsInDataBase() {
        long id = 2123L;
        Optional<TagModel> readModel = tagRepository.findById(id);
        assertTrue(readModel.isEmpty());
    }

    @Test
    void findAll_returnListOfTagsInDatabase_ifExisted() {
        List<TagModel> allTags = List.of(spaTag, relaxTag, gamingTag, lgbtTag);

        List<TagModel> readTags = tagRepository.findAll();
        assertEquals(readTags.size(), allTags.size());
        assertTrue(readTags.containsAll(allTags));
    }

    @Test
    void findAll_returnEmptyList_ifNoTagsExistInDatabase() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, TAG_TABLE);

        List<TagModel> readTags = tagRepository.findAll();
        assertEquals(0, readTags.size());
    }


    @Test
    void delete() {
        tagRepository.delete(relaxTag.getId());
        entityManager.flush();

        long count = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, TAG_TABLE,
                String.format("%s = %d AND %s = '%s'", ID.getColumnName(), relaxTag.getId(),
                        NAME.getColumnName(), relaxTag.getName()));
        assertEquals(0, count);
    }

    @Test
    void delete_shouldNotHaveSideEffects_ifDeleteExistedRow() {
        tagRepository.delete(lgbtTag.getId());
        entityManager.flush();

        long countExisted = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, TAG_TABLE,
                String.format("%s = %d AND %s = '%s'", ID.getColumnName(), spaTag.getId(),
                        NAME.getColumnName(), spaTag.getName()));
        assertEquals(1, countExisted);

        long countDeleted = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, TAG_TABLE,
                String.format("%s = %d AND %s = '%s'", ID.getColumnName(), lgbtTag.getId(),
                        NAME.getColumnName(), lgbtTag.getName()));
        assertEquals(0, countDeleted);
    }

    @Test
    void delete_shouldNotHaveSideEffects_ifDeleteNotExistedRow() {
        try {
            tagRepository.delete(spaTag.getId() + 111);
            entityManager.flush();

        } catch (EmptyResultDataAccessException ignored) {
        }
        long countExisted = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, TAG_TABLE,
                String.format("%s = %d AND %s = '%s'", ID.getColumnName(), spaTag.getId(),
                        NAME.getColumnName(), spaTag.getName()));
        assertEquals(1, countExisted);
    }

    @Test
    void existsById_returnTrue_ifExistsInDatabase() {
        assertTrue(tagRepository.existsById(spaTag.getId()));
    }

    @Test
    void existsById_returnFalse_ifNotExistsInDatabase() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, TAG_TABLE);
        entityManager.clear();

        assertFalse(tagRepository.existsById(1L));
    }

    @Test
    void existsByName_returnTrue_ifExistsInDatabase() {
        assertTrue(tagRepository.existsByName(spaTag.getName()));
    }

    @Test
    void existsByName_returnFalse_ifNotExistsInDatabase() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, TAG_TABLE);

        assertFalse(tagRepository.existsByName("notExistedName123321"));
    }

    @Test
    void findMostWidelyUsedTagsOfUsersById_shouldReturnValidUserAndTags() {
        AppUserModel vovaUser = new AppUserModel(1L, "vova@gmail.com", null);
        UserWithMostlyUsedTagsModel userWithMostlyUsedTagsModel =
                new UserWithMostlyUsedTagsModel(vovaUser,
                        List.of(spaTag, lgbtTag));
        List<UserWithMostlyUsedTagsModel> expectedResult = List.of(userWithMostlyUsedTagsModel);

        assertIterableEquals(expectedResult, tagRepository.findMostWidelyUsedTagsOfUsersById(List.of(vovaUser)));
    }
}