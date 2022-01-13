package com.epam.esm.gcs.persistence.repository;

import com.epam.esm.gcs.persistence.exception.RepositoryException;
import com.epam.esm.gcs.persistence.model.TagModel;

public interface TagRepository extends CrdRepository<TagModel> {

    Boolean existsByName(String name) throws RepositoryException;
}
