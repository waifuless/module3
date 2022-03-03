package com.epam.esm.gcs.persistence.repository;

import java.util.Optional;

/**
 * Base interface for repository with archive functions
 *
 * @param <T> - type of model
 */
public interface ArchiverRepository<T> {

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
     */
    void archive(Long id);

    /**
     * Archives entity by specified id and make successor for it with specified modifications
     *
     * @param idToArchive   - id of entity to archive
     * @param modifications - all not null fields will be set to successor of entity
     * @return successor of entity
     */
    T archiveAndCreateSuccessor(Long idToArchive, T modifications);
}
