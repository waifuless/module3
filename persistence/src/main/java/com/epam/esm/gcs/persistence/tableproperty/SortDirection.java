package com.epam.esm.gcs.persistence.tableproperty;

import java.util.Arrays;
import java.util.Optional;

public enum SortDirection {

    ASC, DESC;

    public static Optional<SortDirection> valueOfIgnoreCase(String direction) {
        return Arrays.stream(values())
                .filter(val -> val.name().equalsIgnoreCase(direction))
                .findAny();
    }
}
