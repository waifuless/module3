package com.epam.esm.gcs.persistence.tableproperty;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TagColumn {

    ID("id"),
    NAME("name");

    private final String columnName;
}
