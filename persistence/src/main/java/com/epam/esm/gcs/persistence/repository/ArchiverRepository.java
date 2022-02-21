package com.epam.esm.gcs.persistence.repository;

import java.util.Optional;

public interface ArchiverRepository<T> {

    Optional<Long> findActualId(Long id);

    void archive(Long id);

    T archiveAndCreateSuccessor(Long idToArchive, T modifications);
}
