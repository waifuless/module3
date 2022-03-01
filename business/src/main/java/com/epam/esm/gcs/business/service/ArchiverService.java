package com.epam.esm.gcs.business.service;

import com.epam.esm.gcs.business.exception.EntitiesArchivedException;
import com.epam.esm.gcs.business.exception.EntityNotFoundException;

import java.util.Optional;

/**
 * Base interface for service with archive functions
 *
 * @param <T> - type of dto
 */
public interface ArchiverService<T> {

    /**
     * Finds actual successor and returns its id
     *
     * @param id - id of entity to search for successors
     * @return - id of actual successor. If not found returns Optional.empty()
     */
    Optional<Long> findActualId(Long id);

    /**
     * Archives entity by specified id
     *
     * @param id - id of entity to archive
     * @throws EntityNotFoundException - if entity does not exist
     */
    void archive(Long id);

    /**
     * Archives entity by specified id and make successor for it with specified modifications
     *
     * @param idToArchive   - id of entity to archive
     * @param modifications - all not null fields will be set to successor of entity
     * @return successor of entity
     * @throws EntitiesArchivedException - if entityToArchive already archived
     * @throws EntityNotFoundException   - if entityToArchive not found
     */
    T archiveAndCreateSuccessor(Long idToArchive, T modifications);
}
