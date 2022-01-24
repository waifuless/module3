package com.epam.esm.gcs.persistence.mapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum GiftCertificateColumn {

    ID("id"),
    NAME("name"),
    DESCRIPTION("description"),
    PRICE("price"),
    DURATION("duration"),
    CREATE_DATE("create_date"),
    LAST_UPDATE_DATE("last_update_date");

    private final String columnName;
}
