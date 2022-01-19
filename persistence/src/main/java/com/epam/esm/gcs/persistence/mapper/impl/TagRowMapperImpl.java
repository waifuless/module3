package com.epam.esm.gcs.persistence.mapper.impl;

import com.epam.esm.gcs.persistence.mapper.TagRowMapper;
import com.epam.esm.gcs.persistence.model.TagModel;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.epam.esm.gcs.persistence.mapper.TagColumn.ID;
import static com.epam.esm.gcs.persistence.mapper.TagColumn.NAME;

@Component
public class TagRowMapperImpl implements TagRowMapper {

    @Override
    public TagModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new TagModel(rs.getLong(ID.getColumnName()), rs.getString(NAME.getColumnName()));
    }
}
