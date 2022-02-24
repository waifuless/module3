package com.epam.esm.gcs.persistence.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pair<T, S> {

    private T first;
    private S second;

    public static <T, S> Pair<T, S> of(T first, S second) {
        return new Pair<>(first, second);
    }
}
