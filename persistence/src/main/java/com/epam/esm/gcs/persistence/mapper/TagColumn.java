package com.epam.esm.gcs.persistence.mapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TagColumn {

    ID("id"),
    NAME("name");

    private final String columnName;
}
