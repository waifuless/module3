package com.epam.esm.gcs.business.service;

import com.epam.esm.gcs.business.dto.TagDto;

import java.util.List;

/**
 * Service interface that contains all methods for interaction with TagDto
 */
public interface TagService extends CrdService<TagDto> {

    /**
     * Finds all TagDtos
     *
     * @return List of all TagDtos
     */
    List<TagDto> findAll();

    /**
     * Checks the existence of TagDto with some name
     *
     * @param name - name for search
     * @return true - if TagDto with @param name exists, false - if TagDto with @param name does not exist
     */
    boolean existsByName(String name);

    /**
     * Checks the existence of TagDto by name. If it doesn't exist, creates it.
     *
     * @param tagToFind - tagDto for search (if id not null - search by id, otherwise search by name and if
     *                  tag with that name does not exist, creates tag)
     * @return - found or created TagDto
     */
    TagDto findOrCreate(TagDto tagToFind);
}
