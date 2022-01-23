package com.epam.esm.gcs.persistence.repository;

import com.epam.esm.gcs.persistence.model.TagModel;

import java.util.Optional;

public interface TagRepository extends CrdRepository<TagModel> {

    Boolean existsByName(String name);

    Optional<TagModel> findByName(String name);
}
