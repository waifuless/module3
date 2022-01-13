package com.epam.esm.gcs.persistence.model;

import lombok.*;

@Data
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TagModel {

    Long id;
    String name;

    public TagModel(String name){
        this.name = name;
    }
}
