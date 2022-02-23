package com.epam.esm.gcs.persistence.repository;

import com.epam.esm.gcs.persistence.model.AppUserModel;
import com.epam.esm.gcs.persistence.model.TagModel;
import com.epam.esm.gcs.persistence.model.UserWithMostlyUsedTagsModel;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface that contains all methods for interaction with TagModel
 */
public interface TagRepository extends CrRepository<TagModel>, DeleteRepository {

    /**
     * Checks the existence of TagModel with some name
     *
     * @param name - name for search
     * @return true - if TagModel with @param name exists, false - if TagModel with @param name does not exist
     */
    Boolean existsByName(String name);

    /**
     * Finds Optional.Model by name. (Optional.empty if model not found)
     *
     * @param name - name Of Model to find
     * @return found Optional.Model or Optional.empty if model not found
     */
    Optional<TagModel> findByName(String name);

    List<UserWithMostlyUsedTagsModel> findMostWidelyUsedTagsOfUsersById(List<AppUserModel> users);
}
