package com.epam.esm.gcs.business.service;

import java.util.Optional;

public interface ArchiverService<T> {

    Optional<Long> findActualId(Long id);

    void archive(Long id);

    T archiveAndCreateSuccessor(Long idToArchive, T modifications);
}
