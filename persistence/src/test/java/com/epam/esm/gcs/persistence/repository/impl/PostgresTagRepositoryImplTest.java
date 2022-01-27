package com.epam.esm.gcs.persistence.repository.impl;

import com.epam.esm.gcs.persistence.model.TagModel;
import com.epam.esm.gcs.persistence.repository.TagRepository;
import com.epam.esm.gcs.persistence.testmanager.TestTablesManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.epam.esm.gcs.persistence.tableproperty.TagColumn.ID;
import static com.epam.esm.gcs.persistence.tableproperty.TagColumn.NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"/test-config.xml"})
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class PostgresTagRepositoryImplTest {

    private final static String TAG_TABLE = "tag";

    private final TagRepository tagRepository;
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public PostgresTagRepositoryImplTest(JdbcTemplate jdbcTemplate, TagRepository tagRepository,
                                         TestTablesManager testTablesManager) throws SQLException {
        this.tagRepository = tagRepository;
        this.jdbcTemplate = jdbcTemplate;
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName(TAG_TABLE)
                .usingGeneratedKeyColumns(ID.getColumnName()).usingColumns(NAME.getColumnName());
        testTablesManager.createOrCleanTables();
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
        String name = "name";
        Long id = createWithName(name);
        TagModel createdModel = new TagModel(id, name);

        Optional<TagModel> readModel = tagRepository.findById(id);
        assertTrue(readModel.isPresent());
        assertEquals(createdModel, readModel.get());
    }

    @Test
    void findById_returnOptionalEmpty_ifDoNotExistsInDataBase() {
        long id = 2123L;
        Optional<TagModel> readModel = tagRepository.findById(id);
        assertTrue(readModel.isEmpty());
    }

    @Test
    void findAll_returnListOfTagsInDatabase_ifExisted() {
        String tagName1 = "name1";
        String tagName2 = "name2";
        Long id1 = createWithName(tagName1);
        Long id2 = createWithName(tagName2);
        List<TagModel> tagsInDatabase = new ArrayList<>();
        tagsInDatabase.add(new TagModel(id1, tagName1));
        tagsInDatabase.add(new TagModel(id2, tagName2));

        List<TagModel> readTags = tagRepository.findAll();
        assertEquals(tagsInDatabase.size(), readTags.size());
        assertIterableEquals(tagsInDatabase, readTags);
    }

    @Test
    void findAll_returnEmptyList_ifNoTagsExistInDatabase() {
        List<TagModel> readTags = tagRepository.findAll();
        assertEquals(0, readTags.size());
    }


    @Test
    void delete() {
        String name = "name";
        Long id = createWithName(name);
        TagModel modelInDatabase = new TagModel(id, name);

        tagRepository.delete(modelInDatabase.getId());

        long count = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, TAG_TABLE,
                String.format("%s = %d AND %s = '%s'", ID.getColumnName(), modelInDatabase.getId(),
                        NAME.getColumnName(), modelInDatabase.getName()));
        assertEquals(0, count);
    }

    @Test
    void delete_shouldNotHaveSideEffects_ifDeleteExistedRow() {
        String tagName1 = "name1";
        String tagName2 = "name2";
        Long id1 = createWithName(tagName1);
        Long id2 = createWithName(tagName2);
        TagModel modelInDatabase1 = new TagModel(id1, tagName1);
        TagModel modelInDatabase2 = new TagModel(id2, tagName2);

        tagRepository.delete(modelInDatabase1.getId());

        long countExisted = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, TAG_TABLE,
                String.format("%s = %d AND %s = '%s'", ID.getColumnName(), modelInDatabase2.getId(),
                        NAME.getColumnName(), modelInDatabase2.getName()));
        assertEquals(1, countExisted);

        long countDeleted = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, TAG_TABLE,
                String.format("%s = %d AND %s = '%s'", ID.getColumnName(), modelInDatabase1.getId(),
                        NAME.getColumnName(), modelInDatabase1.getName()));
        assertEquals(0, countDeleted);
    }

    @Test
    void delete_shouldNotHaveSideEffects_ifDeleteNotExistedRow() {
        String tagName = "name";
        Long id = createWithName(tagName);
        TagModel modelInDatabase = new TagModel(id, tagName);

        tagRepository.delete(modelInDatabase.getId() + 111);

        long countExisted = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, TAG_TABLE,
                String.format("%s = %d AND %s = '%s'", ID.getColumnName(), modelInDatabase.getId(),
                        NAME.getColumnName(), modelInDatabase.getName()));
        assertEquals(1, countExisted);
    }

    @Test
    void existsById_returnTrue_ifExistsInDatabase() {
        String tagName = "name";
        Long id = createWithName(tagName);
        TagModel modelInDatabase = new TagModel(id, tagName);

        assertTrue(tagRepository.existsById(modelInDatabase.getId()));
    }

    @Test
    void existsById_returnFalse_ifNotExistsInDatabase() {
        assertFalse(tagRepository.existsById(1L));
    }

    @Test
    void existsByName_returnTrue_ifExistsInDatabase() {
        String tagName = "name";
        Long id = createWithName(tagName);
        TagModel modelInDatabase = new TagModel(id, tagName);

        assertTrue(tagRepository.existsByName(modelInDatabase.getName()));
    }

    @Test
    void existsByName_returnFalse_ifNotExistsInDatabase() {
        assertFalse(tagRepository.existsByName("someName"));
    }

    private Long createWithName(String name) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(NAME.getColumnName(), name);
        return jdbcInsert.executeAndReturnKey(parameters).longValue();
    }
}