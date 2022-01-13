package com.epam.esm.gcs.persistence.repository.impl;

import com.epam.esm.gcs.persistence.mapper.TagRowMapper;
import com.epam.esm.gcs.persistence.model.TagModel;
import com.epam.esm.gcs.persistence.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class PostgresTagRepositoryImpl implements TagRepository {

    private final static String ID_COLUMN = "id";
    private final static String NAME_COLUMN = "name";
    private final static String TABLE_NAME = "tag";

    private final static String FIND_ALL_QUERY = "SELECT id as id, name as name FROM tag";
    private final static String FIND_BY_ID_QUERY = FIND_ALL_QUERY + " WHERE id = ?";
    private final static String DELETE_QUERY = "DELETE FROM tag WHERE id = ?";
    private final static String EXISTS_BY_ID_QUERY = "SELECT (EXISTS(SELECT 1 FROM tag WHERE id = ?))";
    private final static String EXISTS_BY_NAME_QUERY = "SELECT (EXISTS(SELECT 1 FROM tag WHERE name = ?))";

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final TagRowMapper tagRowMapper;

    @Autowired
    private PostgresTagRepositoryImpl(DataSource dataSource, TagRowMapper tagRowMapper) {
        this.dataSource = dataSource;
        this.tagRowMapper = tagRowMapper;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(ID_COLUMN).usingColumns(NAME_COLUMN);
    }

    @Override
    public TagModel save(TagModel tagModel) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(NAME_COLUMN, tagModel.getName());
        tagModel.setId(jdbcInsert.executeAndReturnKey(parameters).longValue());
        return tagModel;
    }

    @Override
    public Optional<TagModel> findById(long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID_QUERY,
                    new Object[]{id}, new int[]{Types.BIGINT}, tagRowMapper));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<TagModel> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, tagRowMapper);
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update(DELETE_QUERY, id);
    }

    @Override
    public Boolean existsById(long id) {
        return jdbcTemplate.queryForObject(EXISTS_BY_ID_QUERY,
                new Object[]{id}, new int[]{Types.BOOLEAN},
                Boolean.class);
    }

    @Override
    public Boolean existsByName(String name) {
        return jdbcTemplate.queryForObject(EXISTS_BY_NAME_QUERY,
                new Object[]{name}, new int[]{Types.VARCHAR},
                Boolean.class);
    }
}
