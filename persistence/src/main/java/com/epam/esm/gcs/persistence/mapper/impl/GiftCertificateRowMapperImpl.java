package com.epam.esm.gcs.persistence.mapper.impl;

import com.epam.esm.gcs.persistence.mapper.GiftCertificateRowMapper;
import com.epam.esm.gcs.persistence.model.GiftCertificateModel;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static com.epam.esm.gcs.persistence.mapper.GiftCertificateColumn.CREATE_DATE;
import static com.epam.esm.gcs.persistence.mapper.GiftCertificateColumn.DESCRIPTION;
import static com.epam.esm.gcs.persistence.mapper.GiftCertificateColumn.DURATION;
import static com.epam.esm.gcs.persistence.mapper.GiftCertificateColumn.ID;
import static com.epam.esm.gcs.persistence.mapper.GiftCertificateColumn.LAST_UPDATE_DATE;
import static com.epam.esm.gcs.persistence.mapper.GiftCertificateColumn.NAME;
import static com.epam.esm.gcs.persistence.mapper.GiftCertificateColumn.PRICE;

@Component
public class GiftCertificateRowMapperImpl implements GiftCertificateRowMapper {

    @Override
    public GiftCertificateModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        return GiftCertificateModel.builder()
                .id(rs.getLong(ID.getColumnName()))
                .name(rs.getString(NAME.getColumnName()))
                .description(rs.getString(DESCRIPTION.getColumnName()))
                .price(rs.getBigDecimal(PRICE.getColumnName()))
                .duration(rs.getInt(DURATION.getColumnName()))
                .createDate(rs.getObject(CREATE_DATE.getColumnName(), LocalDateTime.class))
                .lastUpdateDate(rs.getObject(LAST_UPDATE_DATE.getColumnName(), LocalDateTime.class))
                .build();
    }
}
