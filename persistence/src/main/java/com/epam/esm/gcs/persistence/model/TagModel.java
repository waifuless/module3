package com.epam.esm.gcs.persistence.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TagModel {

    @With
    private Long id;
    private String name;

    public TagModel(String name) {
        this.name = name;
    }
}
