package com.epam.esm.gcs.persistence.mapper;

public enum TagColumn {

    ID("id"),
    NAME("name");

    private final String columnName;

    TagColumn(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }
}
