package com.epam.esm.gcs.business.mapper;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

public interface Mapper<M, D> {

    M toModel(@NonNull D dto);

    D toDto(@NonNull M model);

    default List<M> toModel(@NonNull List<D> dtos) {
        List<M> models = new ArrayList<>();
        for (D dto : dtos) {
            models.add(toModel(dto));
        }
        return models;
    }

    default List<D> toDto(@NonNull List<M> models) {
        List<D> dtos = new ArrayList<>();
        for (M model : models) {
            dtos.add(toDto(model));
        }
        return dtos;
    }
}
