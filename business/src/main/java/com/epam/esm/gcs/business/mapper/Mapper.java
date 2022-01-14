package com.epam.esm.gcs.business.mapper;

import lombok.NonNull;

import java.util.List;
import java.util.stream.Collectors;

public interface Mapper<M, D> {

    M toModel(@NonNull D dto);

    D toDto(@NonNull M model);

    default List<M> toModel(@NonNull List<D> dtos) {
        return dtos.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    default List<D> toDto(@NonNull List<M> models) {
        return models.stream()
                .map(this::toDto)
                .collect(Collectors.toList());

    }
}
