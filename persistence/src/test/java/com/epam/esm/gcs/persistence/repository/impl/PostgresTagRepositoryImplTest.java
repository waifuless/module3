package com.epam.esm.gcs.persistence.repository.impl;

import com.epam.esm.gcs.persistence.model.TagModel;
import com.epam.esm.gcs.persistence.repository.TagRepository;
import com.epam.esm.gcs.persistence.testmanager.TestTablesManager;
import lombok.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

import static com.epam.esm.gcs.persistence.mapper.TagColumn.ID;
import static com.epam.esm.gcs.persistence.mapper.TagColumn.NAME;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"/test-config.xml"})
class PostgresTagRepositoryImplTest {

    private final TagRepository tagRepository;
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public PostgresTagRepositoryImplTest(DataSource dataSource, TagRepository tagRepository,
                                         TestTablesManager testTablesManager) throws SQLException {
        this.tagRepository = tagRepository;
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("tag")
                .usingGeneratedKeyColumns(ID.getColumnName()).usingColumns(NAME.getColumnName());
        testTablesManager.createOrCleanTables();
    }

    @Test
    void create_returnTagWithIdAndSameName() {
        TagModel inputModel = new TagModel("someVerySimpleName");
        TagModel returnedModel = tagRepository.create(inputModel);
        assertTrue(returnedModel.getId() != null && returnedModel.getId() > 0);
        assertEquals(inputModel.getName(), returnedModel.getName());
    }

    @Test
    void create_returnedTagModelEqualsModelInDatabase() {
        TagModel inputModel = new TagModel("lola");
        TagModel returnedModel = tagRepository.create(inputModel);
        Optional<TagModel> readModel = readById(returnedModel.getId());
        assertTrue(readModel.isPresent());
        assertEquals(returnedModel.getName(), readModel.get().getName());
    }

    @Test
    void findById_returnModel_ifExistInDatabase() {
        String name = "name";
        Long id = create(new TagModel(name));
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
        Long id1 = create(new TagModel(tagName1));
        Long id2 = create(new TagModel(tagName2));
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
        Long id = create(new TagModel(name));
        TagModel modelInDatabase = new TagModel(id, name);

        tagRepository.delete(modelInDatabase.getId());
        Optional<TagModel> tagModel = readById(modelInDatabase.getId());
        assertTrue(tagModel.isEmpty());
    }

    @Test
    void delete_shouldNotHaveSideEffects_ifDeleteExistedRow() {
        String tagName1 = "name1";
        String tagName2 = "name2";
        Long id1 = create(new TagModel(tagName1));
        Long id2 = create(new TagModel(tagName2));
        TagModel modelInDatabase1 = new TagModel(id1, tagName1);
        TagModel modelInDatabase2 = new TagModel(id2, tagName2);

        tagRepository.delete(modelInDatabase1.getId());
        Optional<TagModel> tagModel = readById(modelInDatabase2.getId());
        assertTrue(tagModel.isPresent());
    }

    @Test
    void delete_shouldNotHaveSideEffects_ifDeleteNotExistedRow() {
        String tagName = "name";
        Long id = create(new TagModel(tagName));
        TagModel modelInDatabase = new TagModel(id, tagName);

        tagRepository.delete(modelInDatabase.getId() + 111);
        Optional<TagModel> tagModel = readById(modelInDatabase.getId());
        assertTrue(tagModel.isPresent());
    }

    @Test
    void existsById_returnTrue_ifExistsInDatabase() {
        String tagName = "name";
        Long id = create(new TagModel(tagName));
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
        Long id = create(new TagModel(tagName));
        TagModel modelInDatabase = new TagModel(id, tagName);

        assertTrue(tagRepository.existsByName(modelInDatabase.getName()));
    }

    @Test
    void existsByName_returnFalse_ifNotExistsInDatabase() {
        assertFalse(tagRepository.existsByName("someName"));
    }

    private Optional<TagModel> readById(long id) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject("SELECT id as id, name as name FROM tag WHERE id = ?",
                            new Object[]{id}, new int[]{Types.BIGINT}, new RowMapper<>() {
                                @Override
                                public TagModel mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
                                    return new TagModel(rs.getLong(ID.getColumnName()),
                                            rs.getString(NAME.getColumnName()));
                                }
                            }));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    private Long create(TagModel tagModel) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(NAME.getColumnName(), tagModel.getName());
        return jdbcInsert.executeAndReturnKey(parameters).longValue();
    }
}