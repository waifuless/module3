package com.epam.esm.gcs.persistence.mapper.impl;

import com.epam.esm.gcs.persistence.mapper.TagRowMapper;
import com.epam.esm.gcs.persistence.model.TagModel;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TagRowMapperImpl implements TagRowMapper {

    @Override
    public TagModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        //todo: move attributes to enum
        return new TagModel(rs.getLong("id"), rs.getString("name"));
    }
}
