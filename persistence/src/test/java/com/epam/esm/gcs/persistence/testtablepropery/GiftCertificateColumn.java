package com.epam.esm.gcs.persistence.testtablepropery;

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
    LAST_UPDATE_DATE("last_update_date"),
    STATE_ID("state_id"),
    COUNT("count"),
    SUCCESSOR_ID("successor_id");

    private final String columnName;
}
